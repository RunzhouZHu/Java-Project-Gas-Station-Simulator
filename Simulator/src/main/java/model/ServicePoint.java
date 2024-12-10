package model;

import eduni.distributions.ContinuousGenerator;
import framework.Clock;
import framework.Event;
import framework.EventList;
import framework.Trace;

import java.util.LinkedList;
/**
 * The ServicePoint class represents a service point in the gas station simulation.
 * It manages the queue of customers, handles the beginning and completion of services,
 * and tracks the number of arrived and served customers.
 */
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
    /**
     * Constructs a ServicePoint with the specified generator, event list, and event type to be scheduled.
     *
     * @param generator the generator for service times
     * @param eventList the event list to which events are added
     * @param eventTypeScheduled the event type to be scheduled
     */
    public ServicePoint(ContinuousGenerator generator, EventList eventList, EventType eventTypeScheduled) {
        this.generator = generator;
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
     * Removes and returns the next customer from the queue, and increments the count of served customers.
     *
     * @return the next customer in the queue
     */
    public Customer removeQueue() {
        servedCustomer++;
        reserved = false;
        return queue.poll();
    }
    /**
     * Begins service for the next customer in the queue and schedules the next event.
     */
    public void beginService() {
        Trace.out(Trace.Level.INFO, "Starting a new service for the customer " + queue.peek().getId());

        reserved = true;
        double serviceTime = generator.sample();
        eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getClock() + serviceTime));

        switch (eventTypeScheduled) {
            case REFUELLING -> {
                assert queue.peek() != null;
                queue.peek().setEnterRefuelTime(Clock.getInstance().getClock());
            }
            case WASHING -> {
                assert queue.peek() != null;
                queue.peek().setEnterWashingTime(Clock.getInstance().getClock());
            }
            case SHOPPING -> {
                assert queue.peek() != null;
                queue.peek().setEnterShopTime(Clock.getInstance().getClock());
            }
            case DRYING -> {
                assert queue.peek() != null;
                queue.peek().setEnterDryingTime(Clock.getInstance().getClock());
            }
            case PAYING -> {
                assert queue.peek() != null;
                queue.peek().setEnterCashTime(Clock.getInstance().getClock());
            }
        }
    }
    /**
     * Checks if the service point is currently reserved.
     *
     * @return true if the service point is reserved, false otherwise
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
     * Returns the size of the queue.
     *
     * @return the size of the queue
     */
    public Integer getQueueSize() {
        return queue.size();
    }
    /**
     * Returns the number of customers that have arrived at the service point.
     *
     * @return the number of arrived customers
     */
    public Integer getNumberOfArrivedCustomer() {
        return arrivedCustomer;
    }
    /**
     * Returns the number of customers that have been served at the service point.
     *
     * @return the number of served customers
     */
    public Integer getNumberOfServedCustomer() {
        return servedCustomer;
    }
    /**
     * Returns the event type scheduled for the service point.
     *
     * @return the event type scheduled
     */
    public EventType getEventType() {
        return eventTypeScheduled;
    }
}
