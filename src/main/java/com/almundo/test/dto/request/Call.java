package com.almundo.test.dto.request;

import com.almundo.test.employee.Employee;

public class Call {

    private String clientName;
    private Employee attendedBy;

    public Call(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    @Override
    public String toString() {
        return "Call{" +
                "clientName='" + clientName + '\'' +
                ", attendedBy=" + attendedBy +
                '}';
    }

    public void setAttendedBy(Employee attendedBy) {
        this.attendedBy = attendedBy;
    }

    public Employee getAttendedBy() {
        return attendedBy;
    }
}
