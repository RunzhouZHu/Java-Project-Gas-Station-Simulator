package model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import framework.Clock;
import framework.EventList;
import framework.Trace;
import framework.Trace.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServicePointTest {

    private ServicePoint servicePoint;
    private EventList eventList;
    private ContinuousGenerator generator;

    @BeforeEach
    void setUp() {
        eventList = new EventList();
        generator = new Normal(5, 1); // Example generator with mean 5 and std deviation 1
        servicePoint = new ServicePoint(generator, eventList, EventType.REFUELLING);
        Trace.setTraceLevel(Level.INFO); // Initialize trace level
    }

    @Test
    void testAddQueue() {
        Customer customer = new Customer();
        servicePoint.addQueue(customer);
        assertEquals(1, servicePoint.getNumberOfArrivedCustomer(), "Number of arrived customers should be 1");
        assertTrue(servicePoint.isOnQueue(), "Queue should not be empty after adding a customer");
    }

    @Test
    void testRemoveQueue() {
        Customer customer = new Customer();
        servicePoint.addQueue(customer);
        Customer removedCustomer = servicePoint.removeQueue();
        assertEquals(customer, removedCustomer, "Removed customer should be the same as the added customer");
        assertFalse(servicePoint.isOnQueue(), "Queue should be empty after removing the customer");
    }

    @Test
    void testBeginService() {
        Customer customer = new Customer();
        servicePoint.addQueue(customer);
        servicePoint.beginService();
        assertTrue(servicePoint.isReserved(), "Service point should be reserved after beginning service");

    }

    @Test
    void testIsReserved() {
        assertFalse(servicePoint.isReserved(), "Service point should not be reserved initially");
        Customer customer = new Customer();
        servicePoint.addQueue(customer);
        servicePoint.beginService();
        assertTrue(servicePoint.isReserved(), "Service point should be reserved after beginning service");
    }

    @Test
    void testIsOnQueue() {
        assertFalse(servicePoint.isOnQueue(), "Queue should be empty initially");
        Customer customer = new Customer();
        servicePoint.addQueue(customer);
        assertTrue(servicePoint.isOnQueue(), "Queue should not be empty after adding a customer");
    }

    @Test
    void testGetNumberOfArrivedCustomer() {
        assertEquals(0, servicePoint.getNumberOfArrivedCustomer(), "Number of arrived customers should be 0 initially");
        Customer customer = new Customer();
        servicePoint.addQueue(customer);
        assertEquals(1, servicePoint.getNumberOfArrivedCustomer(), "Number of arrived customers should be 1 after adding a customer");
    }

    @Test
    void testGetNumberOfServedCustomer() {
        assertEquals(0, servicePoint.getNumberOfServedCustomer(), "Number of served customers should be 0 initially");
        Customer customer = new Customer();
        servicePoint.addQueue(customer);
        servicePoint.removeQueue();
        assertEquals(1, servicePoint.getNumberOfServedCustomer(), "Number of served customers should be 1 after serving a customer");
    }
}