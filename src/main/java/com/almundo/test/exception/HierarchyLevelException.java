package com.almundo.test.exception;

import com.almundo.test.employee.Employee;

public class HierarchyLevelException extends InternalServerException {

    private HierarchyLevelException(String message) {
        super(message);
    }

    private HierarchyLevelException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void currentLevelSameAsNext() {
        throw new HierarchyLevelException("Next hierarchy level cannot be the same as current hierachy level.");
    }

    public static HierarchyLevelException levelCouldNotBeInitialized(Throwable cause) {
        return new HierarchyLevelException("Could not initialize the new employee for hierarchy level.", cause);
    }

    public static void failedWhenPuttingCallOnHold(Throwable cause) {
        throw new HierarchyLevelException("Could not put the call on hold because of the employees unavailability.", cause);
    }

    public static void failedWhileAttendingCall(Throwable cause) {
        throw new HierarchyLevelException("Call was lost during the process.", cause);
    }

    public static HierarchyLevelException employeeWasNotEnqueued(Employee employee) {
        return new HierarchyLevelException(employee.getClass().getSimpleName() + " was not enqueued, it will not be available anymore.");
    }
}
