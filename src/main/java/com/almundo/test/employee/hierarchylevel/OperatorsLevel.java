package com.almundo.test.employee.hierarchylevel;

import com.almundo.test.employee.Operator;

public class OperatorsLevel extends EmployeesLevel {

    private OperatorsLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableOperators) {
        super(nextHierarchyLevel, numOfAvailableOperators, OperatorsLevel.class, Operator.class);
    }

    public static OperatorsLevel newHierarchyLevel(EmployeesLevel nextHierarchyLevel, int numOfAvailableOperators) {
        return new OperatorsLevel(nextHierarchyLevel, numOfAvailableOperators);
    }

}
