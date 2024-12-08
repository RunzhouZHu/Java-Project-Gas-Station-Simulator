package model;

import eduni.distributions.ContinuousGenerator;
import framework.Clock;
import framework.Event;
import framework.EventList;
import framework.Trace;

import java.util.LinkedList;

public class ServicePoint {
    private LinkedList<Customer> queue = new LinkedList<>();
    private ContinuousGenerator generator;
    private EventList eventList;
    private EventType eventTypeScheduled;
    // QueueStrategy strategy; // option: ordering of the customer
    private boolean reserved = false;

    //
    private Integer arrivedCustomer = 0;
    private Integer servedCustomer = 0;

    public ServicePoint(ContinuousGenerator generator, EventList eventList, EventType eventTypeScheduled) {
        this.generator = generator;
        this.eventList = eventList;
        this.eventTypeScheduled = eventTypeScheduled;
    }

    public void addQueue(Customer customer) {
        arrivedCustomer++;
        queue.add(customer);
    }

    public Customer removeQueue() {
        servedCustomer++;
        reserved = false;
        return queue.poll();
    }

    public void beginService() {
        Trace.out(Trace.Level.INFO, "Starting a new service for the customer " + queue.peek().getId());

        reserved = true;
        double serviceTime = generator.sample();
        eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getClock() + serviceTime));
    }

    public boolean isReserved() {
        return reserved;
    }

    public boolean isOnQueue() {
        return !queue.isEmpty();
    }

    public Integer getQueueSize() {
        return queue.size();
    }

    public Integer getNumberOfArrivedCustomer() {
        return arrivedCustomer;
    }
    public Integer getNumberOfServedCustomer() {
        return servedCustomer;
    }
}
