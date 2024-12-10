package model;

import framework.*;

import java.util.LinkedList;

/**
 * The Router class is responsible for managing the queue of customers and routing them to the next event.
 * It handles the addition and removal of customers from the queue and begins service for the next customer.
 */
public class Router {
    private final LinkedList<Customer> queue = new LinkedList<>();
    private EventList eventList = new EventList();
    private EventType eventTypeScheduled;
    private boolean reserved = false;
    private Integer arrivedCustomer = 0;
    /**
     * Constructs a Router with the specified event list and event type to be scheduled.
     *
     * @param eventList the event list to which events are added
     * @param eventTypeScheduled the event type to be scheduled
     */
    public Router(EventList eventList, EventType eventTypeScheduled) {
        this.eventList = eventList;
        this.eventTypeScheduled = eventTypeScheduled;
    }
    /**
     * Adds a customer to the queue and increments the count of arrived customers.
     *
     * @param customer the customer to be added to the queue
     */
    public void addQueue(Customer customer) {
        arrivedCustomer++;
        queue.add(customer);
    }

    /**
     * Removes and returns the next customer from the queue.
     *
     * @return the next customer in the queue
     */
    public Customer removeQueue() {
        reserved = false;
        return queue.poll();
    }
    /**
     * Begins service for the next customer in the queue and schedules the next event.
     */
    public void beginService() {
        Trace.out(Trace.Level.INFO, "Starting a new service for the customer #" + queue.peek().getId());
        reserved = true;

        eventList.add(new Event(eventTypeScheduled, 1));
    }
    /**
     * Checks if the router is currently reserved.
     *
     * @return true if the router is reserved, false otherwise
     */
    public boolean isReserved() {
        return reserved;
    }
    /**
     * Checks if there are customers in the queue.
     *
     * @return true if there are customers in the queue, false otherwise
     */
    public boolean isOnQueue() {
        return !queue.isEmpty();
    }
    /**
     * Returns the number of customers that have arrived at the router.
     *
     * @return the number of arrived customers
     */
    public Integer getNumberOfArrivedCustomer() {
        return arrivedCustomer;
    }
}
