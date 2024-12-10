package framework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {
    private Engine engine;

    @BeforeEach
    void setUp() {
        engine = new Engine();
    }

    @Test
    void testSetSimulationTime() {
        engine.setSimulationTime(100.0);
        assertEquals(100.0, engine.getSimulationTime());
    }

    @Test
    void testRun() {
        // This test would need to be more comprehensive, potentially using mocks for EventList and Clock
        engine.setSimulationTime(10.0);
        engine.run();
        assertTrue(engine.getClock().getClock() <= 10.0);
    }

    @Test
    void testPauseAndResume() {
        engine.pause();
        assertTrue(engine.getPauseStatus());

        engine.resume();
        assertFalse(engine.getPauseStatus());
    }

    @Test
    void testGetClock() {
        assertNotNull(engine.getClock());
    }

    @Test
    void testGetEventList() {
        assertNotNull(engine.getEventList());
    }

    @Test
    void testSimulate() {
        engine.setSimulationTime(10.0);
        assertTrue(engine.simulate());

        engine.getClock().setClock(10.0);
        assertFalse(engine.simulate());
    }
}