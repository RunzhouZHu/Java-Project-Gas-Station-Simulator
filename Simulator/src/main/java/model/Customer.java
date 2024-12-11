package model;

import framework.Clock;
import framework.Trace;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Represents a customer in the simulation.
 * Each customer has an arrival time, removal time, and a unique ID.
 * Customers can visit various service points and have their actions recorded.
 */
public class Customer {
    private double arrivalTime;
    private double removalTime;
    private int id;

    private static int nextId = 1;
    private static long sum = 0;

    // Customer action records
    private double arriveTime = 0.0;

    private double lineRefuelTime = 0.0;
    private double enterRefuelTime = 0.0;
    private double exitRefuelTime = 0.0;

    private double lineWashTime = 0.0;
    private double enterWashingTime = 0.0;
    private double exitWashingTime = 0.0;

    private double lineShopTime = 0.0;
    private double enterShopTime = 0.0;
    private double exitShopTime = 0.0;

    private double lineDryingTime = 0.0;
    private double enterDryingTime = 0.0;
    private double exitDryingTime = 0.0;

    private double lineCashTime = 0.0;
    private double enterCashTime = 0.0;
    private double exitCashTime = 0.0;


    // Set the which service points the customer want to visit
    // There are total 5 event types: DP1 ~ DP5
    private ArrayList<EventType> eventTypesToVisit = new ArrayList<EventType>();
    private RandomChooserForCustomer randomChooserForCustomer = new RandomChooserForCustomer(new HashMap<>(){{
        // Here to decide the possibility a customer visit each service point
        put(EventType.REFUELLING, 0.5); // Possibility to visit gas station
        put(EventType.WASHING, 0.5); // Possibility to visit gas station
        put(EventType.SHOPPING, 0.5); // Possibility to visit gas station
        put(EventType.DRYING, 0.5); // Possibility to visit gas station
    }});
    /**
     * Constructs a new Customer with a unique ID.
     */
    public Customer() {
        id = nextId++;

        // arrivalTime = Clock.getInstance().getClock();
        // removalTime = arrivalTime;
        // Trace.out(Trace.Level.INFO, "New customer " + id + " arrivalTime=" + arrivalTime);
    }

    /*
    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    } */

    //public void setRemovalTime(double removalTime) {
    /**
     * Sets the removal time of the customer.
     */
    public void setRemovalTime() {
        //this.removalTime = removalTime;
        this.removalTime += Clock.getInstance().getClock();
    }

    /**
     * Returns the arrival time of the customer.
     *
     * @return the arrival time
     */
    public double getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Returns the removal time of the customer.
     *
     * @return the removal time
     */
    public double getRemovalTime() {
        return removalTime;
    }

    /**
     * Returns the unique ID of the customer.
     *
     * @return the customer ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the list of event types the customer wants to visit.
     *
     * @return the list of event types
     */
    public ArrayList<EventType> getEventTypesToVisit() {
        return eventTypesToVisit;
    }

    /**
     * Sets the event types the customer wants to visit based on predefined probabilities.
     */
    public void setEventTypesToVisit() {
        eventTypesToVisit = randomChooserForCustomer.choose();
        if (eventTypesToVisit.isEmpty()) {
            eventTypesToVisit.add(EventType.REFUELLING);
        }
    }
    /**
     * Marks the service of a specific event type as finished for the customer.
     *
     * @param eventType the event type to finish
     * @throws InterruptedException if the thread is interrupted
     */
    public void finishService(EventType eventType) throws InterruptedException {
        // Thread.sleep(1000);
        if (eventTypesToVisit.contains(eventType)) {
            eventTypesToVisit.remove(eventType);
            setRemovalTime();
        } else if (eventTypesToVisit.contains(EventType.DRYING) && !eventTypesToVisit.contains(EventType.WASHING)) {
            setRemovalTime();
            System.out.println("Attention, the customer ordered the dryer, so the washing is added default.");
        } else {
            System.err.println("ERROR! The customer do not want this service!!");
            System.err.println("Customer " + id + eventType);
        }
    }
    /**
     * Reports the results of the customer's actions.
     */
    public void reportResults() {
        /*
        framework.Trace.out(framework.Trace.Level.INFO, "\nCustomer " + id + " ready! ");
        framework.Trace.out(framework.Trace.Level.INFO, "Customer " + id + " arrived: " + arrivalTime);
        framework.Trace.out(framework.Trace.Level.INFO, "Customer " + id + " removed: " + removalTime);
        framework.Trace.out(framework.Trace.Level.INFO, "Customer " + id + " stayed: " + (removalTime - arrivalTime));

        sum += (long) (removalTime - arrivalTime);
        double mean = (double) sum / id;
        System.out.println("Current mean of the customer service times " + mean);

         */

        System.out.println("Customer report results called.");
    }

    public double getArriveTime() {
        return arriveTime;
    }
    public void setArriveTime(double arriveTime) {
        this.arriveTime = arriveTime;
    }
    public double getEnterRefuelTime() {
        return enterRefuelTime;
    }
    public void setEnterRefuelTime(double enterRefuelTime) {
        this.enterRefuelTime = enterRefuelTime;
    }
    public double getExitRefuelTime() {
        return exitRefuelTime;
    }
    public void setExitRefuelTime(double exitRefuelTime) {
        this.exitRefuelTime = exitRefuelTime;
    }
    public double getEnterWashingTime() {
        return enterWashingTime;
    }
    public void setEnterWashingTime(double enterWashingTime) {
        this.enterWashingTime = enterWashingTime;
    }
    public double getExitWashingTime() {
        return exitWashingTime;
    }
    public void setExitWashingTime(double exitWashingTime) {
        this.exitWashingTime = exitWashingTime;
    }
    public double getEnterShopTime() {
        return enterShopTime;
    }
    public void setEnterShopTime(double enterShopTime) {
        this.enterShopTime = enterShopTime;
    }
    public double getExitShopTime() {
        return exitShopTime;
    }
    public void setExitShopTime(double exitShopTime) {
        this.exitShopTime = exitShopTime;
    }
    public double getEnterDryingTime() {
        return enterDryingTime;
    }
    public void setEnterDryingTime(double enterDryingTime) {
        this.enterDryingTime = enterDryingTime;
    }
    public double getExitDryingTime() {
        return exitDryingTime;
    }
    public void setExitDryingTime(double exitDryingTime) {
        this.exitDryingTime = exitDryingTime;
    }
    public double getEnterCashTime() {
        return enterCashTime;
    }
    public void setEnterCashTime(double enterCashTime) {
        this.enterCashTime = enterCashTime;
    }
    public double getExitCashTime() {
        return exitCashTime;
    }
    public void setExitCashTime(double exitCashTime) {
        this.exitCashTime = exitCashTime;
    }

    public double getLineRefuelTime() {
        return lineRefuelTime;
    }

    public void setLineRefuelTime(double lineRefuelTime) {
        this.lineRefuelTime = lineRefuelTime;
    }

    public double getLineWashTime() {
        return lineWashTime;
    }

    public void setLineWashTime(double lineWashTime) {
        this.lineWashTime = lineWashTime;
    }

    public double getLineDryingTime() {
        return lineDryingTime;
    }

    public void setLineDryingTime(double linesDryingTime) {
        this.lineDryingTime = linesDryingTime;
    }

    public double getLineCashTime() {
        return lineCashTime;
    }

    public void setLineCashTime(double lineCashTime) {
        this.lineCashTime = lineCashTime;
    }

    public double getLineShopTime() {
        return lineShopTime;
    }

    public void setLineShopTime(double lineShopTime) {
        this.lineShopTime = lineShopTime;
    }
}
