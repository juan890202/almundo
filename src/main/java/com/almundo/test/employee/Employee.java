package com.almundo.test.employee;

public class Employee {

    protected String greeting;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "greeting='" + greeting + '\'' +
                '}';
    }

}
