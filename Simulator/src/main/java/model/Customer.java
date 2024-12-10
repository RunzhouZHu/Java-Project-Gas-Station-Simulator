package model;

import framework.Clock;
import framework.Trace;

import controller.CarController;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer {
    private double arrivalTime;
    private double removalTime;
    private int id;

    private static int nextId = 1;
    private static long sum = 0;
    private CarController carController = new CarController();

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
    public void setRemovalTime() {
        //this.removalTime = removalTime;
        this.removalTime += Clock.getInstance().getClock();
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

    public CarController getCarController() {
        return carController;
    }

    public ArrayList<EventType> getEventTypesToVisit() {
        return eventTypesToVisit;
    }
    public void setEventTypesToVisit() {
        eventTypesToVisit = randomChooserForCustomer.choose();
        if (eventTypesToVisit.isEmpty()) {
            eventTypesToVisit.add(EventType.REFUELLING);
        }
    }
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
