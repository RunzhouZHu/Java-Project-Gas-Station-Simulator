package model;

import framework.*;

import java.util.HashMap;
import java.util.LinkedList;

public class Router {
    private final LinkedList<Customer> queue = new LinkedList<>();
    private EventList eventList = new EventList();
    private EventType eventTypeScheduled;
    private boolean reserved = false;

    public Router(EventList eventList, EventType eventTypeScheduled) {
        this.eventList = eventList;
        this.eventTypeScheduled = eventTypeScheduled;
    }

    public void addQueue(Customer customer) {
        queue.add(customer);
    }

    public Customer removeQueue() {
        reserved = false;
        return queue.poll();
    }

    public void beginService() {
        Trace.out(Trace.Level.INFO, "Starting a new service for the customer #" + queue.peek().getId());
        reserved = true;

        eventList.add(new Event(eventTypeScheduled, 0));
    }

    public boolean isReserved() {
        return reserved;
    }

    public boolean isOnQueue() {
        return !queue.isEmpty();
    }
}
