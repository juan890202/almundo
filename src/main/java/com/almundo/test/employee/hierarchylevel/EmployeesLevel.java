package com.almundo.test.employee.hierarchylevel;

import com.almundo.test.employee.Employee;
import com.almundo.test.exception.HierarchyLevelException;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class EmployeesLevel {

    private Class<? extends Employee> typeOfEmployeesInThisLevel;
    private Option<EmployeesLevel> nextHierarchyLevel;
    private BlockingQueue<Employee> employees;
    private BiPredicate<EmployeesLevel, Class<? extends EmployeesLevel>> levelsAreNotSame =
            (nextLevel, currentLevelType) -> nextLevel == null || !currentLevelType.isAssignableFrom(nextLevel.getClass());

    protected EmployeesLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees,
                             Class<? extends EmployeesLevel> currentHierarchyLevelType, Class<? extends Employee> employeesType) {
        this.typeOfEmployeesInThisLevel = employeesType;
        this.nextHierarchyLevel = nextHierarchyLevel == null ? Option.none()
                : checkNextHierarchyLevel(nextHierarchyLevel, currentHierarchyLevelType);
        this.employees = initializeHierarchyLevel(numOfAvailableEmployees, employeesType);
    }

    private Option<EmployeesLevel> checkNextHierarchyLevel(EmployeesLevel nextHierarchyLevel, Class<? extends EmployeesLevel> levelClassType) {
        return Option.of(nextHierarchyLevel)
                .filter(nextLevel -> levelsAreNotSame.test(nextLevel, levelClassType))
                .onEmpty(HierarchyLevelException::currentLevelSameAsNext);
    }

    private BlockingQueue<Employee> initializeHierarchyLevel(int numOfAvailableEmployees, Class<? extends Employee> employee) {
        return IntStream.range(0, numOfAvailableEmployees)
                .mapToObj(i -> Try.of(employee::newInstance).getOrElseThrow(HierarchyLevelException::levelCouldNotBeInitialized))
                .collect(Collectors.toCollection(() -> new ArrayBlockingQueue<>(numOfAvailableEmployees)));
    }

    public Option<Employee> getAvailableEmployee() {
        return Option.of(this.employees.poll())
                .orElse((() -> nextHierarchyLevel.flatMap(EmployeesLevel::getAvailableEmployee)));
    }

    public void returnEmployeeToQueue(Employee employee) {
        Option.of(employee)
                .filter(emp -> typeOfEmployeesInThisLevel.equals(emp.getClass()))
                .onEmpty(() -> nextHierarchyLevel.get().returnEmployeeToQueue(employee))
                .filter(employees::offer)
                .onEmpty(() -> HierarchyLevelException.employeeWasNotEnqueued(employee));
    }

}
