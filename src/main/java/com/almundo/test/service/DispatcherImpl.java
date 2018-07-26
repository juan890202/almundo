package com.almundo.test.service;

import com.almundo.test.dto.request.Call;
import com.almundo.test.dto.response.ErrorResponse;
import com.almundo.test.dto.response.Response;
import com.almundo.test.dto.response.SuccessResponse;
import com.almundo.test.employee.Employee;
import com.almundo.test.employee.hierarchylevel.EmployeesLevel;
import com.almundo.test.event.EmployeesAvailabilityTopic;
import com.almundo.test.exception.HierarchyLevelException;
import io.vavr.CheckedRunnable;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DispatcherImpl implements Dispatcher {

    private ExecutorService executorService;
    private Call call;
    private EmployeesAvailabilityTopic employeesAvailability;
    private EmployeesLevel employeesLevel;

    public DispatcherImpl(Call call, EmployeesAvailabilityTopic employeesAvailability, EmployeesLevel employeesLevel) {
        this.executorService = Executors.newFixedThreadPool(10);
        this.call = call;
        this.employeesAvailability = employeesAvailability;
        this.employeesLevel = employeesLevel;
    }

    @Override
    public synchronized Response dispatchCall() {
        Try<Employee> dispatch =
                employeesLevel.getAvailableEmployee()
                        .orElse(
                                () -> Try.run(() -> employeesAvailability.notifyUnavailability(this))
                                        .andThen(() -> System.out.println("There is no availability... Enqueuing call of client: " + call.getClientName()))
                                        .andThenTry((CheckedRunnable) this::wait).onFailure(HierarchyLevelException::failedWhenPuttingCallOnHold)
                                        .transform(Void -> employeesLevel.getAvailableEmployee()))
                        .toTry()
                        .mapTry(employee -> employee.receiveCall(call))
                        .andThenTry(employeesLevel::returnEmployeeToQueue)
                        .andFinally(() -> executorService.execute(employeesAvailability::notifyAvailability));

        return Option.of(dispatch)
                .filter(Try::isFailure)
                .toTry()
                .map(employeeTry -> employeeTry.failed().map(exception -> ErrorResponse.newResponse(call, exception)))
                .getOrElse(() -> Try.of(() -> SuccessResponse.newResponse(call)))
                .get();
    }

}
