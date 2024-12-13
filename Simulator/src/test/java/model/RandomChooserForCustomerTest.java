package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class RandomChooserForCustomerTest {

    private RandomChooserForCustomer chooser;

    @BeforeEach
    void setUp() {
        HashMap<EventType, Double> eventTypes = new HashMap<>();
        eventTypes.put(EventType.REFUELLING, 0.5);
        eventTypes.put(EventType.WASHING, 0.5);
        eventTypes.put(EventType.SHOPPING, 0.5);
        eventTypes.put(EventType.PAYING, 0.5);
        eventTypes.put(EventType.DRYING, 0.5);

        chooser = new RandomChooserForCustomer(eventTypes);
    }

    @Test
    void testChoose() {
        ArrayList<EventType> chosenEvents = chooser.choose();
        assertNotNull(chosenEvents, "Chosen events should not be null");
        assertTrue(chosenEvents.size() <= 5, "Chosen events size should be less than or equal to 5");
    }

    @Test
    void testChooseProbability() {
        int refuellingCount = 0;
        int washingCount = 0;
        int shoppingCount = 0;
        int payingCount = 0;
        int dryingCount = 0;

        for (int i = 0; i < 1000; i++) {
            ArrayList<EventType> chosenEvents = chooser.choose();
            if (chosenEvents.contains(EventType.REFUELLING)) refuellingCount++;
            if (chosenEvents.contains(EventType.WASHING)) washingCount++;
            if (chosenEvents.contains(EventType.SHOPPING)) shoppingCount++;
            if (chosenEvents.contains(EventType.PAYING)) payingCount++;
            if (chosenEvents.contains(EventType.DRYING)) dryingCount++;
        }

        assertTrue(refuellingCount > 0, "Refuelling event should be chosen at least once");
        assertTrue(washingCount > 0, "Washing event should be chosen at least once");
        assertTrue(shoppingCount > 0, "Shopping event should be chosen at least once");
        assertTrue(payingCount > 0, "Paying event should be chosen at least once");
        assertTrue(dryingCount > 0, "Drying event should be chosen at least once");
    }
}