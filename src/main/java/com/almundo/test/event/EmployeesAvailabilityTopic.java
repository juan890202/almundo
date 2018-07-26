package com.almundo.test.event;

import com.almundo.test.service.Dispatcher;

public interface EmployeesAvailabilityTopic {

    void notifyAvailability();

    void notifyUnavailability(Dispatcher dispatcher);

}
