package com.almundo.test.employee.hierarchylevel;

import com.almundo.test.employee.Director;

public class DirectorsLevel extends EmployeesLevel {

    private DirectorsLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableDirectors) {
        super(nextHierarchyLevel, numOfAvailableDirectors, DirectorsLevel.class, Director.class);
    }

    public static DirectorsLevel newHierarchyLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableDirectors) {
        return new DirectorsLevel(nextHierarchyLevel, numOfAvailableDirectors);
    }

}
