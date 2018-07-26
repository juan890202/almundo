package com.almundo.test.employee.hierarchylevel;

import com.almundo.test.employee.Operator;
import com.almundo.test.exception.HierarchyLevelException;
import io.vavr.control.Option;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class OperatorsLevelTest {

    private static final String NEXT_LEVEL_FIELD_NAME = "nextHierarchyLevel";
    private static final String EMPLOYEES_QUEUE_FIELD_NAME = "employees";

    @Test
    public void shouldCreate_TheOperatorsHierarchyLevel() throws NoSuchFieldException, IllegalAccessException {
        EmployeesLevel nextHierarchyLevel = null;
        int numOfOperatorsAvailable = 5;

        OperatorsLevel operatorsLevel = OperatorsLevel.newHierarchyLevel(nextHierarchyLevel, numOfOperatorsAvailable);
        assertNotNull(operatorsLevel);

        Class<? extends EmployeesLevel> operatorsLevelClass = operatorsLevel.getClass();
        Field nextLevelField = operatorsLevelClass.getSuperclass().getDeclaredField(NEXT_LEVEL_FIELD_NAME);
        nextLevelField.setAccessible(true);
        Field employeesQueueField = operatorsLevelClass.getSuperclass().getDeclaredField(EMPLOYEES_QUEUE_FIELD_NAME);
        employeesQueueField.setAccessible(true);

        Option<EmployeesLevel> actualNextHierarchyLevel = (Option<EmployeesLevel>) nextLevelField.get(operatorsLevel);
        BlockingQueue<Operator> actualEmployeesQueue = (BlockingQueue<Operator>) employeesQueueField.get(operatorsLevel);

        assertTrue(actualNextHierarchyLevel.isEmpty());
        assertThat(actualEmployeesQueue.size(), is(numOfOperatorsAvailable));
    }

    @Test
    public void shouldCreate_TheOperatorsHierarchyLevel_WithHigherHierarchyLevel() throws NoSuchFieldException, IllegalAccessException {
        EmployeesLevel nextHierarchyLevel = mock(SupervisorsLevel.class);
        int numOfOperatorsAvailable = 5;

        OperatorsLevel operatorsLevel = OperatorsLevel.newHierarchyLevel(nextHierarchyLevel, numOfOperatorsAvailable);
        assertNotNull(operatorsLevel);

        Class<? extends EmployeesLevel> operatorsLevelClass = operatorsLevel.getClass();
        Field nextLevelField = operatorsLevelClass.getSuperclass().getDeclaredField(NEXT_LEVEL_FIELD_NAME);
        nextLevelField.setAccessible(true);
        Field employeesQueueField = operatorsLevelClass.getSuperclass().getDeclaredField(EMPLOYEES_QUEUE_FIELD_NAME);
        employeesQueueField.setAccessible(true);

        Option<EmployeesLevel> actualNextHierarchyLevel = (Option<EmployeesLevel>) nextLevelField.get(operatorsLevel);
        BlockingQueue<Operator> actualEmployeesQueue = (BlockingQueue<Operator>) employeesQueueField.get(operatorsLevel);

        assertTrue(actualNextHierarchyLevel.get().getClass().isInstance(nextHierarchyLevel));
        assertThat(actualEmployeesQueue.size(), is(numOfOperatorsAvailable));
    }

    @Test(expected = HierarchyLevelException.class)
    public void shouldThrowError_WhenCurrentAndNextLevelsAreEqual() {
        int numOfOperatorsAvailable = 5;
        EmployeesLevel nextHierarchyLevel = OperatorsLevel.newHierarchyLevel(null, numOfOperatorsAvailable);
        OperatorsLevel.newHierarchyLevel(nextHierarchyLevel, numOfOperatorsAvailable);
    }

}
