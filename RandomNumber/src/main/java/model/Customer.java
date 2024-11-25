package model;

import framework.Clock;
import framework.Trace;

public class Customer {
    private double arrivalTime;
    private double removalTime;
    private int id;

    private static int i = 1;
    private static long sum = 0;

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

    public void reportResults() {
        simu.framework.Trace.out(simu.framework.Trace.Level.INFO, "\nCustomer " + id + " ready! ");
        simu.framework.Trace.out(simu.framework.Trace.Level.INFO, "Customer " + id + " arrived: " + arrivalTime);
        simu.framework.Trace.out(simu.framework.Trace.Level.INFO, "Customer " + id + " removed: " + removalTime);
        simu.framework.Trace.out(simu.framework.Trace.Level.INFO, "Customer " + id + " stayed: " + (removalTime - arrivalTime));

        sum += (long) (removalTime - arrivalTime);
        double mean = (double) sum / id;
        System.out.println("Current mean of the customer service times " + mean);
    }

}
