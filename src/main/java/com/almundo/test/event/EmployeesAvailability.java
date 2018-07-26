package com.almundo.test.event;

import com.almundo.test.service.Dispatcher;
import io.vavr.control.Option;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EmployeesAvailability implements EmployeesAvailabilityTopic {

    private BlockingQueue<Dispatcher> enqueuedDispatchers;

    private EmployeesAvailability() {
        enqueuedDispatchers = new LinkedBlockingQueue<>();
    }

    private static class EmployeesAvailabilityHolder {
        private EmployeesAvailabilityHolder() {}
        private static final EmployeesAvailability INSTANCE = new EmployeesAvailability();
    }

    public static EmployeesAvailability getInstance() {
        return EmployeesAvailabilityHolder.INSTANCE;
    }

    @Override
    public void notifyAvailability() {
        Dispatcher enqueuedDispatcher = enqueuedDispatchers.poll();
        System.out.println("availability.....");
        Option.of(enqueuedDispatcher)
                .onEmpty(() -> System.out.println("There are no more enqueued calls!!!"))
                .peek(dispatcher -> {
                    synchronized (dispatcher) {
                        dispatcher.notifyAll();
                    }
                });

    }

    @Override
    public void notifyUnavailability(Dispatcher dispatcher) {
        enqueuedDispatchers.offer(dispatcher);
    }

}
