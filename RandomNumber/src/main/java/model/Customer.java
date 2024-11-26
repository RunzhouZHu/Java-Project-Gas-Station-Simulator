package model;

import framework.Clock;
import framework.RandomChooserForCustomer;
import framework.Trace;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer {
    private double arrivalTime;
    private double removalTime;
    private int id;

    private static int i = 1;
    private static long sum = 0;

    // Set the which service points the customer want to visit
    // There are total 5 event types: DP1 ~ DP5
    private ArrayList<EventType> eventTypesToVisit = new ArrayList<EventType>();
    private RandomChooserForCustomer randomChooserForCustomer = new RandomChooserForCustomer(new HashMap<>(){{
        // Here to decide the possibility a customer visit each service point
        put(EventType.DEP1, 0.5); // Possibility to visit gas station
        put(EventType.DEP2, 0.5); // Possibility to visit gas station
        put(EventType.DEP3, 0.5); // Possibility to visit gas station
        put(EventType.DEP4, 0.5); // Possibility to visit gas station
        put(EventType.DEP5, 0.5); // Possibility to visit gas station
    }});

    public Customer() {
        id = i++;

        arrivalTime = Clock.getInstance().getClock();
        Trace.out(Trace.Level.INFO, "New customer " + id + " arrivalTime=" + arrivalTime);
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setRemovalTime(double removalTime) {
        this.removalTime = removalTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public double getRemovalTime() {
        return removalTime;
    }

    public int getId() {
        return id;
    }

    public ArrayList<EventType> getEventTypesToVisit() {
        return eventTypesToVisit;
    }
    public void setEventTypesToVisit() {
        eventTypesToVisit = randomChooserForCustomer.choose();
    }
    public void finishService(EventType eventType) {
        if (eventTypesToVisit.contains(eventType)) {
            eventTypesToVisit.remove(eventType);
        } else {
            System.err.println("ERROR! The customer do not want this service!!");
        }
        System.out.println("Customer " + id + eventTypesToVisit);

    }

    public void reportResults() {
        framework.Trace.out(framework.Trace.Level.INFO, "\nCustomer " + id + " ready! ");
        framework.Trace.out(framework.Trace.Level.INFO, "Customer " + id + " arrived: " + arrivalTime);
        framework.Trace.out(framework.Trace.Level.INFO, "Customer " + id + " removed: " + removalTime);
        framework.Trace.out(framework.Trace.Level.INFO, "Customer " + id + " stayed: " + (removalTime - arrivalTime));

        sum += (long) (removalTime - arrivalTime);
        double mean = (double) sum / id;
        System.out.println("Current mean of the customer service times " + mean);
    }

}
