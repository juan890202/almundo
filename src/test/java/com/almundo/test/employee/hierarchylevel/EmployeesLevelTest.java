package com.almundo.test.employee.hierarchylevel;

import com.almundo.test.employee.Employee;
import com.almundo.test.employee.Operator;
import com.almundo.test.employee.Supervisor;
import io.vavr.control.Option;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EmployeesLevel.class)
public class EmployeesLevelTest {

    private static final String EMPLOYEES_QUEUE_FIELD_NAME = "employees";
    private static final String NEXT_HIERARCHY_FIELD_NAME = "nextHierarchyLevel";
    private static final String TYPE_OF_EMPLOYEES_FIELD_NAME = "typeOfEmployeesInThisLevel";

    @Test
    public void operatorShouldAttendCall() throws InterruptedException {
        EmployeesLevel employeesLevelMock = mock(EmployeesLevel.class);

        BlockingQueue<Operator> operatorsQueueMock = new ArrayBlockingQueue<>(1);
        Operator operator = mock(Operator.class);
        operatorsQueueMock.put(operator);
        Whitebox.setInternalState(employeesLevelMock, EMPLOYEES_QUEUE_FIELD_NAME, operatorsQueueMock);

        PowerMockito.doCallRealMethod().when(employeesLevelMock).getAvailableEmployee();

        Option<Employee> availableEmployee = employeesLevelMock.getAvailableEmployee();
        assertTrue(availableEmployee.isDefined());
        assertThat(availableEmployee.get(), is(equalTo(operator)));
    }

    @Test
    public void thereAreNoEmployeesToAttendTheCall() {
        EmployeesLevel employeesLevelMock = mock(OperatorsLevel.class);

        BlockingQueue<Operator> operatorsQueueMock = new ArrayBlockingQueue<>(1);
        Whitebox.setInternalState(employeesLevelMock, EMPLOYEES_QUEUE_FIELD_NAME, operatorsQueueMock);
        Option<EmployeesLevel> nextHierarchyLevelMock = Option.none();
        Whitebox.setInternalState(employeesLevelMock, NEXT_HIERARCHY_FIELD_NAME, nextHierarchyLevelMock);

        PowerMockito.doCallRealMethod().when(employeesLevelMock).getAvailableEmployee();

        Option<Employee> availableEmployee = employeesLevelMock.getAvailableEmployee();
        assertTrue(availableEmployee.isEmpty());
    }

    @Test
    public void thereAreNoEmployeesInTheCurrentLevelToAttendTheCall() {
        EmployeesLevel employeesLevelMock = mock(OperatorsLevel.class);

        BlockingQueue<Operator> operatorsQueueMock = new ArrayBlockingQueue<>(1);
        Whitebox.setInternalState(employeesLevelMock, EMPLOYEES_QUEUE_FIELD_NAME, operatorsQueueMock);
        Option<EmployeesLevel> nextHierarchyLevelMock = Option.of(mock(SupervisorsLevel.class));
        Whitebox.setInternalState(employeesLevelMock, NEXT_HIERARCHY_FIELD_NAME, nextHierarchyLevelMock);
        Supervisor supervisor = mock(Supervisor.class);

        when(nextHierarchyLevelMock.get().getAvailableEmployee()).thenReturn(Option.of(supervisor));
        PowerMockito.doCallRealMethod().when(employeesLevelMock).getAvailableEmployee();

        Option<Employee> availableEmployee = employeesLevelMock.getAvailableEmployee();
        assertThat(availableEmployee, is(equalTo(Option.of(supervisor))));
    }

    @Test
    public void returningEmployeeToTheQueue_AfterAttendingACall() {
        EmployeesLevel employeesLevelMock = mock(OperatorsLevel.class);

        Operator operator = mock(Operator.class);
        Whitebox.setInternalState(employeesLevelMock, TYPE_OF_EMPLOYEES_FIELD_NAME, (Object) operator.getClass());
        BlockingQueue<Operator> operatorsQueueMock = new ArrayBlockingQueue<>(1);
        Whitebox.setInternalState(employeesLevelMock, EMPLOYEES_QUEUE_FIELD_NAME, operatorsQueueMock);

        PowerMockito.doCallRealMethod().when(employeesLevelMock).returnEmployeeToQueue(operator);

        employeesLevelMock.returnEmployeeToQueue(operator);
    }

    @Test
    public void returningHigherLevelEmployeeToTheQueue_AfterAttendingACall() {
        EmployeesLevel employeesLevelMock = mock(OperatorsLevel.class);
        EmployeesLevel employeesNextLevelMock = mock(SupervisorsLevel.class);

        // State for first method call
        Operator operator = mock(Operator.class);
        Whitebox.setInternalState(employeesLevelMock, TYPE_OF_EMPLOYEES_FIELD_NAME, (Object) operator.getClass());
        BlockingQueue<Operator> operatorsQueueMock = new ArrayBlockingQueue<>(1);
        Whitebox.setInternalState(employeesLevelMock, EMPLOYEES_QUEUE_FIELD_NAME, operatorsQueueMock);
        Option<EmployeesLevel> nextHierarchyLevelMock = Option.of(employeesNextLevelMock);
        Whitebox.setInternalState(employeesLevelMock, NEXT_HIERARCHY_FIELD_NAME, nextHierarchyLevelMock);

        // State for second method call
        Supervisor supervisor = mock(Supervisor.class);
        Whitebox.setInternalState(employeesNextLevelMock, TYPE_OF_EMPLOYEES_FIELD_NAME, (Object) supervisor.getClass());
        BlockingQueue<Supervisor> supervisorsQueueMock = new ArrayBlockingQueue<>(1);
        Whitebox.setInternalState(employeesNextLevelMock, EMPLOYEES_QUEUE_FIELD_NAME, supervisorsQueueMock);
        Option<EmployeesLevel> nextHierarchyLevelMockForSupervisors = Option.none();
        Whitebox.setInternalState(employeesNextLevelMock, NEXT_HIERARCHY_FIELD_NAME, nextHierarchyLevelMockForSupervisors);

        PowerMockito.doCallRealMethod().when(employeesLevelMock).returnEmployeeToQueue(supervisor);
        PowerMockito.doCallRealMethod().when(employeesNextLevelMock).returnEmployeeToQueue(supervisor);

        employeesLevelMock.returnEmployeeToQueue(supervisor);
    }

}