package model;

import eduni.distributions.ContinuousGenerator;
import framework.EventList;
import framework.Trace;
import simu.model.EventType;

import java.util.LinkedList;

public class ServicePoint {
    private LinkedList<Customer> queue = new LinkedList<>();
    private ContinuousGenerator generator;
    private EventList eventList;
    private EventType eventTypeScheduled;
    // QueueStrategy strategy; // option: ordering of the customer
    private boolean resered = false;

    public ServicePoint(ContinuousGenerator generator, EventList eventList, EventType eventTypeScheduled) {
        this.generator = generator;
        this.eventList = eventList;
        this.eventTypeScheduled = eventTypeScheduled;
    }

    public void addQueue(Customer customer) {
        queue.add(customer);
    }

    public Customer removeQueue() {
        resered = false;
        return queue.poll();
    }

    public void beginService() {
        Trace.out(Trace.Level.INFO, "Starting a new service for the customer " + queue.peek().getId());

        resered = true;
        double serviceTime = generator.sample();

    }


}
