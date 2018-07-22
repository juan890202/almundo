package com.almundo.test.employee.hierarchylevel;

import com.almundo.test.employee.Employee;

import java.util.concurrent.BlockingQueue;

public abstract class EmployeesLevel {

    private Class<? extends Employee> typeOfEmployeesInThisLevel;
    private EmployeesLevel nextHierarchyLevel;
    private BlockingQueue<Employee> employees;

    protected EmployeesLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableEmployees,
                             Class<? extends EmployeesLevel> currentHierarchyLevelType, Class<? extends Employee> employeesType) {
    }

}
