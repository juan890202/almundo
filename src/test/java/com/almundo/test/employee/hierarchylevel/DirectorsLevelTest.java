package com.almundo.test.employee.hierarchylevel;

import com.almundo.test.employee.Director;
import com.almundo.test.exception.HierarchyLevelException;
import io.vavr.control.Option;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DirectorsLevelTest {

    private static final String NEXT_LEVEL_FIELD_NAME = "nextHierarchyLevel";
    private static final String EMPLOYEES_QUEUE_FIELD_NAME = "employees";

    @Test
    public void shouldCreate_TheDirectorsHierarchyLevel() throws NoSuchFieldException, IllegalAccessException {
        EmployeesLevel nextHierarchyLevel = null;
        int numOfDirectorsAvailable = 5;

        DirectorsLevel directorsLevel = DirectorsLevel.newHierarchyLevel(nextHierarchyLevel, numOfDirectorsAvailable);
        assertNotNull(directorsLevel);

        Class<? extends EmployeesLevel> directorsLevelClass = directorsLevel.getClass();
        Field nextLevelField = directorsLevelClass.getSuperclass().getDeclaredField(NEXT_LEVEL_FIELD_NAME);
        nextLevelField.setAccessible(true);
        Field employeesQueueField = directorsLevelClass.getSuperclass().getDeclaredField(EMPLOYEES_QUEUE_FIELD_NAME);
        employeesQueueField.setAccessible(true);

        Option<EmployeesLevel> actualNextHierarchyLevel = (Option<EmployeesLevel>) nextLevelField.get(directorsLevel);
        BlockingQueue<Director> actualEmployeesQueue = (BlockingQueue<Director>) employeesQueueField.get(directorsLevel);

        assertTrue(actualNextHierarchyLevel.isEmpty());
        assertThat(actualEmployeesQueue.size(), is(numOfDirectorsAvailable));
    }

    @Test
    public void shouldCreate_TheDirectorsHierarchyLevel_WithHigherHierarchyLevel() throws NoSuchFieldException, IllegalAccessException {
        EmployeesLevel nextHierarchyLevel = mock(SupervisorsLevel.class);
        int numOfDirectorsAvailable = 5;

        DirectorsLevel directorsLevel = DirectorsLevel.newHierarchyLevel(nextHierarchyLevel, numOfDirectorsAvailable);
        assertNotNull(directorsLevel);

        Class<? extends EmployeesLevel> directorsLevelClass = directorsLevel.getClass();
        Field nextLevelField = directorsLevelClass.getSuperclass().getDeclaredField(NEXT_LEVEL_FIELD_NAME);
        nextLevelField.setAccessible(true);
        Field employeesQueueField = directorsLevelClass.getSuperclass().getDeclaredField(EMPLOYEES_QUEUE_FIELD_NAME);
        employeesQueueField.setAccessible(true);

        Option<EmployeesLevel> actualNextHierarchyLevel = (Option<EmployeesLevel>) nextLevelField.get(directorsLevel);
        BlockingQueue<Director> actualEmployeesQueue = (BlockingQueue<Director>) employeesQueueField.get(directorsLevel);

        assertTrue(actualNextHierarchyLevel.get().getClass().isInstance(nextHierarchyLevel));
        assertThat(actualEmployeesQueue.size(), is(numOfDirectorsAvailable));
    }

    @Test(expected = HierarchyLevelException.class)
    public void shouldThrowError_WhenCurrentAndNextLevelsAreEqual() {
        int numOfDirectorsAvailable = 5;
        EmployeesLevel nextHierarchyLevel = DirectorsLevel.newHierarchyLevel(null, numOfDirectorsAvailable);
        DirectorsLevel.newHierarchyLevel(nextHierarchyLevel, numOfDirectorsAvailable);
    }

}