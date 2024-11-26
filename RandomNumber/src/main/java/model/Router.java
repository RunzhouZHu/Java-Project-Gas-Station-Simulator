package model;

import framework.*;

import java.util.HashMap;
import java.util.LinkedList;

public class Router {
    private final LinkedList<Customer> queue = new LinkedList<>();

    private EventList eventList = new EventList();

    private HashMap<EventType, Integer> eventTypes = new HashMap<>();

    private boolean reserved = false;

    public Router(EventList eventList, HashMap<EventType, Integer> eventTypes) {
        this.eventList = eventList;
        this.eventTypes = eventTypes;
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

        RandomChooser randomChooser = new RandomChooser(eventTypes);
        reserved = true;
        EventType eventTypeScheduled = randomChooser.choose();
        eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getClock()));
    }

    public boolean isReserved() {
        return reserved;
    }

    public boolean isOnQueue() {
        return !queue.isEmpty();
    }
}
