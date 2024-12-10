package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Result class provides methods to calculate and generate results for the gas station simulation.
 * It includes methods to calculate busy time, wait time, and generate a result file in Markdown format.
 */

public class Result {

    private ArrayList<Customer> customerResults = new ArrayList<>();

    /**
     * Constructs a Result instance with the specified list of customer results.
     *
     * @param customerResults the list of customer results
     */
    public Result(ArrayList<Customer> customerResults) {
        this.customerResults = customerResults;
    }

    /**
     * Calculates the total busy time for all customers.
     *
     * @param customerResults the list of customer results
     * @return the total busy time
     */
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

    /**
     * Calculates the total wait time for all customers.
     *
     * @param customerResults the list of customer results
     * @return the total wait time
     */
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
    /**
     * Generates a result file in Markdown format with the simulation results.
     *
     * @param customerResults the list of customer results
     * @param arrivalCount the number of arrived customers
     */
    public static void generateResultFile(ArrayList<Customer> customerResults,
                                          int arrivalCount,
                                          int completedCount,
                                          double busyTime,
                                          double time,
                                          double servicePointUtilization,
                                          double serviceThroughput,
                                          double serviceTime,
                                          double waitingTime,
                                          double responseTime,
                                          double averageQueueLength) {
        String markdownContent = "# The Result of Gas Station Simulation\n\n" +
                "## Directly observable variables: \n\n" +
                "- A, arrived clients count (arrival count): " + arrivalCount + "\n" +
                "- C, clients serviced count (completed count): " + completedCount + "\n" +
                "- B, active time in service point (busy time): " + String.format("%.2f", busyTime) + "\n" +
                "- T, total simulation time (time): " + String.format("%.2f", time) + "\n" +

                "## Derived variables (from the previous variables) are:\n\n" +
                "- U, service point utilization related to the max capacity, U = B/T: " + String.format("%.2f", servicePointUtilization) + "\n" +
                "- X, service throughput, number of clients serviced related to the time, X = C/T: " + String.format("%.2f", serviceThroughput) + "\n" +
                "- S, service time, average service time in the service point, S = B/C: " + String.format("%.2f", serviceTime) + "\n" +

                "## Additional directly observable variables are:\n\n" +
                "- W, waiting time, cumulative response times sum of all clients: " + String.format("%.2f", waitingTime) + "\n" +

                "## From these last two, we can further derive the following quantities:\n\n" +
                "- R, response time, average throughput time at the service point, R = W/C: " + String.format("%.2f", responseTime) + "\n" +
                "- N, average queue length at the service point (including the served) N = W/T: " + String.format("%.2f", averageQueueLength) + "\n";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("results.md", false))) {
            writer.write(markdownContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Markdown generated");
    }
}
