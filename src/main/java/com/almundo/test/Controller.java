package com.almundo.test;

import com.almundo.test.dto.request.Call;
import com.almundo.test.dto.response.Response;
import com.almundo.test.employee.hierarchylevel.DirectorsLevel;
import com.almundo.test.employee.hierarchylevel.EmployeesLevel;
import com.almundo.test.employee.hierarchylevel.OperatorsLevel;
import com.almundo.test.employee.hierarchylevel.SupervisorsLevel;
import com.almundo.test.event.EmployeesAvailability;
import com.almundo.test.event.EmployeesAvailabilityTopic;
import com.almundo.test.service.Dispatcher;
import com.almundo.test.service.DispatcherImpl;

public class Controller {

    private final EmployeesLevel directorsLevel;
    private final EmployeesLevel supervisorsLevel;
    private final EmployeesLevel operatorsLevel;
    private static final int NUM_OF_DIRECTORS = 1;
    private static final int NUM_OF_SUPERVISORS = 2;
    private static final int NUM_OF_OPERATORS = 7;
    private final EmployeesAvailabilityTopic employeesAvailability;

    private Controller() {
        // Chain of Responsibility
        directorsLevel = DirectorsLevel.newHierarchyLevel(null, NUM_OF_DIRECTORS);
        supervisorsLevel = SupervisorsLevel.newHierarchyLevel(directorsLevel, NUM_OF_SUPERVISORS);
        operatorsLevel = OperatorsLevel.newHierarchyLevel(supervisorsLevel, NUM_OF_OPERATORS);
        // Producer-Consumer
        employeesAvailability = EmployeesAvailability.getInstance();
    }

    private static class ControllerHolder {
        private ControllerHolder() {}
        private static final Controller INSTANCE = new Controller();
    }

    public static Controller getInstance() {
        return ControllerHolder.INSTANCE;
    }

    public Response getRequest(Call call) {
        Dispatcher dispatcher = new DispatcherImpl(call, employeesAvailability, operatorsLevel);
        return dispatcher.dispatchCall();
    }

}
