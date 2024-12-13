package framework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    private Event event;
    private IEventType mockEventType;

    @BeforeEach
    void setUp() {
        mockEventType = new IEventType() {}; // Mock implementation of IEventType
        event = new Event(mockEventType, 5.0);
    }

    @Test
    void testSetAndGetEventType() {
        IEventType newEventType = new IEventType() {}; // Another mock implementation
        event.setEventType(newEventType);
        assertEquals(newEventType, event.getEventType(), "Event type should be set to newEventType");
    }

    @Test
    void testSetAndGetTime() {
        event.setTime(10.0);
        assertEquals(10.0, event.getTime(), "Event time should be set to 10.0");
    }

    @Test
    void testCompareTo() {
        Event earlierEvent = new Event(mockEventType, 3.0);
        Event laterEvent = new Event(mockEventType, 7.0);

        assertTrue(event.compareTo(earlierEvent) > 0, "Event should occur after earlierEvent");
        assertTrue(event.compareTo(laterEvent) < 0, "Event should occur before laterEvent");
        assertEquals(0, event.compareTo(new Event(mockEventType, 5.0)), "Events with the same time should be equal");
    }
}