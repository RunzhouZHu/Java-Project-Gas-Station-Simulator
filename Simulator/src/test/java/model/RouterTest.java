package model;

import framework.Event;
import framework.EventList;
import framework.Trace;
import framework.Trace.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouterTest {

    private Router router;
    private EventList eventList;

    @BeforeEach
    void setUp() {
        eventList = new EventList();
        router = new Router(eventList, EventType.REFUELLING);
        Trace.setTraceLevel(Level.INFO); // Correctly initialize traceLevel
    }

    @Test
    void testAddQueue() {
        Customer customer = new Customer();
        router.addQueue(customer);
        assertEquals(1, router.getNumberOfArrivedCustomer(), "Number of arrived customers should be 1");
        assertTrue(router.isOnQueue(), "Queue should not be empty after adding a customer");
    }

    @Test
    void testRemoveQueue() {
        Customer customer = new Customer();
        router.addQueue(customer);
        Customer removedCustomer = router.removeQueue();
        assertEquals(customer, removedCustomer, "Removed customer should be the same as the added customer");
        assertFalse(router.isOnQueue(), "Queue should be empty after removing the customer");
    }

    @Test
    void testBeginService() {
        Customer customer = new Customer();
        router.addQueue(customer);
        router.beginService();
        assertTrue(router.isReserved(), "Router should be reserved after beginning service");
    }

    @Test
    void testIsReserved() {
        assertFalse(router.isReserved(), "Router should not be reserved initially");
        Customer customer = new Customer();
        router.addQueue(customer);
        router.beginService();
        assertTrue(router.isReserved(), "Router should be reserved after beginning service");
    }

    @Test
    void testIsOnQueue() {
        assertFalse(router.isOnQueue(), "Queue should be empty initially");
        Customer customer = new Customer();
        router.addQueue(customer);
        assertTrue(router.isOnQueue(), "Queue should not be empty after adding a customer");
    }

    @Test
    void testGetNumberOfArrivedCustomer() {
        assertEquals(0, router.getNumberOfArrivedCustomer(), "Number of arrived customers should be 0 initially");
        Customer customer = new Customer();
        router.addQueue(customer);
        assertEquals(1, router.getNumberOfArrivedCustomer(), "Number of arrived customers should be 1 after adding a customer");
    }
}