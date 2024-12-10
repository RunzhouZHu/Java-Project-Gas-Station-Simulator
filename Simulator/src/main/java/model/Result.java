package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

    public static Double waitTime(ArrayList<Customer> customerResults) {
        double waitTime = 0.0;
        for (Customer customer : customerResults) {
            double time = customer.getLineRefuelTime() +
                    customer.getLineWashTime() +
                    customer.getLineDryingTime() +
                    customer.getLineShopTime() +
                    customer.getLineCashTime();
            waitTime += time;
        }
        return waitTime;
    }

    public static void generateResultFile(ArrayList<Customer> customerResults, int arrivalCount) {
        String markdownContent = "# The Result of Gas Station Simulation\n\n" +
                "## Directly observable variables: \n\n" +
                "A, arrived clients count (arrival count): " + arrivalCount;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("results.md", true))) {
            writer.write(markdownContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Markdown generated");
    }
}
