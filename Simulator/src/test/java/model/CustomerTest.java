package model;

import framework.Clock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    private Customer customer;
    private Clock mockClock;


    @BeforeEach
    void setUp() {
        customer = new Customer();
        mockClock = mock(Clock.class);
        Clock.setInstance(mockClock);
    }

    @Test
    void testSetRemovalTime() {
        when(mockClock.getClock()).thenReturn(10.0);
        double initialRemovalTime = customer.getRemovalTime();
        customer.setRemovalTime();
        assertEquals(initialRemovalTime + 10.0, customer.getRemovalTime());
    }

    @Test
    void testGetArrivalTime() {
        assertEquals(0.0, customer.getArrivalTime());
    }

    @Test
    void testGetRemovalTime() {
        assertEquals(0.0, customer.getRemovalTime());
    }

    @Test
    void testGetId() {
        assertTrue(customer.getId() > 0);
    }



    @Test
    void testGetEventTypesToVisit() {
        assertNotNull(customer.getEventTypesToVisit());
    }

    @Test
    void testSetEventTypesToVisit() {
        customer.setEventTypesToVisit();
        assertFalse(customer.getEventTypesToVisit().isEmpty());
    }

    @Test
    void testFinishService() throws InterruptedException {
        customer.setEventTypesToVisit();
        EventType eventType = customer.getEventTypesToVisit().get(0);
        customer.finishService(eventType);
        assertFalse(customer.getEventTypesToVisit().contains(eventType));
    }

    @Test
    void testReportResults() {
        customer.reportResults();
        // Add assertions based on the expected behavior of reportResults
    }

    @Test
    void testGetAndSetArriveTime() {
        customer.setArriveTime(10.0);
        assertEquals(10.0, customer.getArriveTime());
    }

    @Test
    void testGetAndSetEnterRefuelTime() {
        customer.setEnterRefuelTime(10.0);
        assertEquals(10.0, customer.getEnterRefuelTime());
    }

    @Test
    void testGetAndSetExitRefuelTime() {
        customer.setExitRefuelTime(10.0);
        assertEquals(10.0, customer.getExitRefuelTime());
    }

    @Test
    void testGetAndSetEnterWashingTime() {
        customer.setEnterWashingTime(10.0);
        assertEquals(10.0, customer.getEnterWashingTime());
    }

    @Test
    void testGetAndSetExitWashingTime() {
        customer.setExitWashingTime(10.0);
        assertEquals(10.0, customer.getExitWashingTime());
    }

    @Test
    void testGetAndSetEnterShopTime() {
        customer.setEnterShopTime(10.0);
        assertEquals(10.0, customer.getEnterShopTime());
    }

    @Test
    void testGetAndSetExitShopTime() {
        customer.setExitShopTime(10.0);
        assertEquals(10.0, customer.getExitShopTime());
    }

    @Test
    void testGetAndSetEnterDryingTime() {
        customer.setEnterDryingTime(10.0);
        assertEquals(10.0, customer.getEnterDryingTime());
    }

    @Test
    void testGetAndSetExitDryingTime() {
        customer.setExitDryingTime(10.0);
        assertEquals(10.0, customer.getExitDryingTime());
    }

    @Test
    void testGetAndSetEnterCashTime() {
        customer.setEnterCashTime(10.0);
        assertEquals(10.0, customer.getEnterCashTime());
    }

    @Test
    void testGetAndSetExitCashTime() {
        customer.setExitCashTime(10.0);
        assertEquals(10.0, customer.getExitCashTime());
    }

    @Test
    void testGetAndSetLineRefuelTime() {
        customer.setLineRefuelTime(10.0);
        assertEquals(10.0, customer.getLineRefuelTime());
    }

    @Test
    void testGetAndSetLineWashTime() {
        customer.setLineWashTime(10.0);
        assertEquals(10.0, customer.getLineWashTime());
    }

    @Test
    void testGetAndSetLineDryingTime() {
        customer.setLineDryingTime(10.0);
        assertEquals(10.0, customer.getLineDryingTime());
    }

    @Test
    void testGetAndSetLineCashTime() {
        customer.setLineCashTime(10.0);
        assertEquals(10.0, customer.getLineCashTime());
    }

    @Test
    void testGetAndSetLineShopTime() {
        customer.setLineShopTime(10.0);
        assertEquals(10.0, customer.getLineShopTime());
    }
}