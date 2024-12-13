package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class RandomChooserForRouterTest {

    private RandomChooserForRouter chooser;

    @BeforeEach
    void setUp() {
        HashMap<EventType, Integer> eventTypes = new HashMap<>();
        eventTypes.put(EventType.WASHING, 10);
        eventTypes.put(EventType.PAYING, 20);
        eventTypes.put(EventType.DRYING, 30);

        chooser = new RandomChooserForRouter(eventTypes);
    }

    @Test
    void testChoose() {
        EventType chosenEvent = chooser.choose();
        assertNotNull(chosenEvent, "Chosen event should not be null");
        assertTrue(chosenEvent == EventType.WASHING || chosenEvent == EventType.PAYING || chosenEvent == EventType.DRYING, "Chosen event should be one of the predefined event types");
    }

    @Test
    void testChooseProbability() {
        int washingCount = 0;
        int payingCount = 0;
        int dryingCount = 0;

        for (int i = 0; i < 1000; i++) {
            EventType chosenEvent = chooser.choose();
            if (chosenEvent == EventType.WASHING) washingCount++;
            if (chosenEvent == EventType.PAYING) payingCount++;
            if (chosenEvent == EventType.DRYING) dryingCount++;
        }

        assertTrue(washingCount > 0, "Washing event should be chosen at least once");
        assertTrue(payingCount > 0, "Paying event should be chosen at least once");
        assertTrue(dryingCount > 0, "Drying event should be chosen at least once");
    }
}