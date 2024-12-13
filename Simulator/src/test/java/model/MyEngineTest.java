package model;

import framework.Event;
import framework.Trace;
import model.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyEngineTest {
    private MyEngine myEngine;

    @BeforeEach
    void setUp() {
        Trace.setTraceLevel(Trace.Level.INFO); // Initialize traceLevel
        myEngine = new MyEngine(
                5.0, 1.0, // Refuel mean and variance
                5.0, 1.0, // Wash mean and variance
                5.0, 1.0, // Shop mean and variance
                5.0, 1.0, // Pay mean and variance
                5.0, 1.0, // Dry mean and variance
                10.0, 2.0, // Arrival mean and variance
                1.0 // Delay
        );
    }

    @Test
    void testInitialize() {
        myEngine.initialize();
        assertNotNull(myEngine.getArrivalProcesses(), "Arrival process should be initialized");
    }

    @Test
    void testRunEventArrival() {
        Event event = new Event(EventType.ARRIVE, 0.0);
        myEngine.runEvent(event);
        assertEquals(1, myEngine.getArrivedCount(), "Arrived count should be 1 after processing an arrival event");
    }




    @Test
    void testDriveCar() {
        myEngine.driveCar(0, 1);
        assertFalse(myEngine.getCarQueue().isEmpty(), "Car queue should not be empty after driving a car");
    }

}