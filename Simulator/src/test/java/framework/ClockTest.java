package framework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClockTest {
    private Clock clock;

    @BeforeEach
    void setUp() {
        clock = Clock.getInstance();
        clock.setClock(0.0); // Reset clock before each test
    }

    @Test
    void testSingletonInstance() {
        Clock anotherClock = Clock.getInstance();
        assertSame(clock, anotherClock, "Clock instances should be the same (singleton)");
    }

    @Test
    void testSetAndGetClock() {
        clock.setClock(10.0);
        assertEquals(10.0, clock.getClock(), "Clock time should be set to 10.0");
    }

    @Test
    void testSetInstance() {
        Clock newClock = Clock.getInstance();
        newClock.setClock(20.0);
        Clock.setInstance(newClock);
        assertSame(newClock, Clock.getInstance(), "Clock instance should be the new instance");
        assertEquals(20.0, Clock.getInstance().getClock(), "Clock time should be 20.0");
    }
}