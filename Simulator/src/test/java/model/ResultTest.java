package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    private ArrayList<Customer> customerResults;
    private Result result;

    @BeforeEach
    void setUp() {
        customerResults = new ArrayList<>();
        // Add mock customers to the list
        Customer customer1 = new Customer();
        customer1.setEnterRefuelTime(1.0);
        customer1.setExitRefuelTime(2.0);
        customer1.setEnterShopTime(3.0);
        customer1.setExitShopTime(4.0);
        customer1.setEnterWashingTime(5.0);
        customer1.setExitWashingTime(6.0);
        customer1.setEnterDryingTime(7.0);
        customer1.setExitDryingTime(8.0);
        customer1.setEnterCashTime(9.0);
        customer1.setExitCashTime(10.0);
        customer1.setLineRefuelTime(0.5);
        customer1.setLineWashTime(0.5);
        customer1.setLineDryingTime(0.5);
        customer1.setLineShopTime(0.5);
        customer1.setLineCashTime(0.5);

        customerResults.add(customer1);

        result = new Result(customerResults);
    }

    @Test
    void testBusyTime() {
        double busyTime = Result.busyTime(customerResults);
        assertEquals(5.0, busyTime, 0.01, "Busy time should be 5.0");
    }

    @Test
    void testWaitTime() {
        double waitTime = Result.waitTime(customerResults);
        assertEquals(2.5, waitTime, 0.01, "Wait time should be 2.5");
    }

    @Test
    void testGenerateResultFile() {
        // This test will check if the result file is generated without exceptions
        assertDoesNotThrow(() -> Result.generateResultFile(
                customerResults,
                1, // arrivalCount
                1, // completedCount
                10.0, // busyTime
                20.0, // time
                0.5, // servicePointUtilization
                0.05, // serviceThroughput
                10.0, // serviceTime
                2.5, // waitingTime
                2.5, // responseTime
                0.125 // averageQueueLength
        ));
    }
}