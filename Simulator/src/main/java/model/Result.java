package model;

import java.util.ArrayList;

public class Result {

    private ArrayList<Customer> customerResults = new ArrayList<>();

    public Result(ArrayList<Customer> customerResults) {
        this.customerResults = customerResults;
    }

    public static Double busyTime(ArrayList<Customer> customerResults) {
        double busyTime = 0.0;

        for (Customer customer : customerResults) {
            double time = customer.getExitRefuelTime() - customer.getEnterRefuelTime() +
                    customer.getExitShopTime() - customer.getEnterShopTime() +
                    customer.getExitWashingTime() - customer.getEnterWashingTime() +
                    customer.getExitDryingTime() - customer.getEnterDryingTime() +
                    customer.getExitCashTime() - customer.getEnterCashTime();
            busyTime += time;
        }
        return busyTime;
    }
}
