package framework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventListTest {
    private EventList eventList;
    private Event mockEvent1;
    private Event mockEvent2;

    @BeforeEach
    void setUp() {
        Trace.setTraceLevel(Trace.Level.INFO); // Initialize traceLevel
        eventList = new EventList();
        mockEvent1 = mock(Event.class);
        mockEvent2 = mock(Event.class);
    }

    @Test
    void testAddEvent() {
        when(mockEvent1.getEventType()).thenReturn(new IEventType() {});
        when(mockEvent1.getTime()).thenReturn(5.0);

        eventList.add(mockEvent1);
        assertEquals(5.0, eventList.getNextEventTime(), "Next event time should be 5.0");
    }

    @Test
    void testRemoveEvent() {
        when(mockEvent1.getEventType()).thenReturn(new IEventType() {});
        when(mockEvent1.getTime()).thenReturn(5.0);
        when(mockEvent2.getEventType()).thenReturn(new IEventType() {});
        when(mockEvent2.getTime()).thenReturn(10.0);

        eventList.add(mockEvent1);
        eventList.add(mockEvent2);

        Event removedEvent = eventList.remove();
        assertEquals(mockEvent1, removedEvent, "Removed event should be mockEvent1");
        assertEquals(10.0, eventList.getNextEventTime(), "Next event time should be 10.0");
    }

    @Test
    void testGetNextEventTimeWhenEmpty() {
        assertEquals(0.0, eventList.getNextEventTime(), "Next event time should be 0.0 when event list is empty");
    }
}