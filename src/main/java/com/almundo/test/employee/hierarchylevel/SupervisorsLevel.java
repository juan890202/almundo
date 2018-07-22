package com.almundo.test.employee.hierarchylevel;

import com.almundo.test.employee.Supervisor;

public class SupervisorsLevel extends EmployeesLevel {

    private SupervisorsLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableSupervisors) {
        super(nextHierarchyLevel, numOfAvailableSupervisors, SupervisorsLevel.class, Supervisor.class);
    }

    public static SupervisorsLevel newHierarchyLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableSupervisors) {
        return new SupervisorsLevel(nextHierarchyLevel, numOfAvailableSupervisors);
    }

}
