package framework;

import eduni.distributions.Normal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArrivalProcessTest {
    private ArrivalProcess arrivalProcess;
    private Normal mockGenerator;
    private EventList mockEventList;
    private IEventType mockEventType;
    private Clock mockClock;

    @BeforeEach
    void setUp() {
        mockGenerator = mock(Normal.class);
        mockEventList = mock(EventList.class);
        mockEventType = mock(IEventType.class);
        mockClock = mock(Clock.class);
        Clock.setInstance(mockClock);
        arrivalProcess = new ArrivalProcess(mockGenerator, mockEventList, mockEventType);
    }

    @Test
    void testGenerateNextEvent() {
        when(mockGenerator.sample()).thenReturn(5.0);
        when(mockClock.getClock()).thenReturn(10.0);

        arrivalProcess.generateNextEvent();

        verify(mockEventList).add(any(Event.class));
    }
}