package com.almundo.test.service;

import com.almundo.test.dto.request.Call;
import com.almundo.test.dto.response.Response;
import com.almundo.test.employee.Operator;
import com.almundo.test.employee.Supervisor;
import com.almundo.test.employee.hierarchylevel.EmployeesLevel;
import com.almundo.test.event.EmployeesAvailabilityTopic;
import com.almundo.test.exception.HierarchyLevelException;
import io.vavr.control.Option;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class DispatcherImplTest {

    private Call call;
    @Mock
    private ExecutorService executorService;
    @Mock
    private EmployeesAvailabilityTopic employeesAvailability;
    @Mock
    private EmployeesLevel employeesLevel;

    private DispatcherImpl dispatcher;

    @Before
    public void setUp() {
        call = new Call("mock client");
        dispatcher = new DispatcherImpl(call, employeesAvailability, employeesLevel);
    }

    @Test
    public void employeeShouldAttendCall() {
        Operator operator = new Operator();

        when(employeesLevel.getAvailableEmployee()).thenReturn(Option.of(operator));
        doNothing().when(employeesLevel).returnEmployeeToQueue(operator);
        doNothing().when(executorService).execute(any());

        Response response = dispatcher.dispatchCall();

        assertTrue(response.isSuccess());
        assertNotNull(response.getCall());
        assertNotNull(response.getCall().getAttendedBy());
        assertThat(response.getCall().getAttendedBy(), is(equalTo(operator)));
        assertNull(response.getErrorType());
        assertNull(response.getDetails());
    }

    @Test
    public void employeeShouldWait_BeforeAttendingCall() throws InterruptedException, ExecutionException {
        Supervisor supervisor = new Supervisor();

        when(employeesLevel.getAvailableEmployee()).thenReturn(Option.none()).thenReturn(Option.of(supervisor));
        doNothing().when(employeesAvailability).notifyUnavailability(anyObject());
        doNothing().when(employeesLevel).returnEmployeeToQueue(supervisor);
        doNothing().when(executorService).execute(any());

        ExecutorService realExecutorService = Executors.newSingleThreadExecutor();
        Future<Response> responseFuture = realExecutorService.submit(dispatcher::dispatchCall);
        Thread.sleep(2000L);

        synchronized (dispatcher) {
            Option.of(responseFuture)
                    .filter(future -> !future.isDone())
                    .peek(future -> dispatcher.notifyAll());
        }

        Response response = responseFuture.get();

        assertTrue(response.isSuccess());
        assertNotNull(response.getCall());
        assertNotNull(response.getCall().getAttendedBy());
        assertThat(response.getCall().getAttendedBy(), is(equalTo(supervisor)));
        assertNull(response.getErrorType());
        assertNull(response.getDetails());
    }

    @Test
    public void errorWhenPuttingBackTheEmployee_AfterAttendingTheCall() {
        Operator operator = new Operator();
        String expectedErrorMessage = operator.getClass().getSimpleName() + " was not enqueued, it will not be available anymore.";

        when(employeesLevel.getAvailableEmployee()).thenReturn(Option.of(operator));
        doThrow(HierarchyLevelException.employeeWasNotEnqueued(operator)).when(employeesLevel).returnEmployeeToQueue(operator);
        doNothing().when(executorService).execute(any());

        Response response = dispatcher.dispatchCall();

        assertFalse(response.isSuccess());
        assertNotNull(response.getCall());
        assertNotNull(response.getCall().getAttendedBy());
        assertThat(response.getCall().getAttendedBy(), is(equalTo(operator)));
        assertThat(response.getErrorType(), is(HierarchyLevelException.class.getName()));
        assertNotNull(response.getDetails());
        assertThat(response.getMessage(), is(equalTo(expectedErrorMessage)));
    }

}
