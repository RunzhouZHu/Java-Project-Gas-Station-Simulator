package framework;

import model.EventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RandomChooserForCustomer {
    private HashMap<EventType, Double> eventTypes;

    public RandomChooserForCustomer(HashMap<EventType, Double> eventTypes) {
        this.eventTypes = eventTypes;
        for (EventType eventType : eventTypes.keySet()) {
            if (eventTypes.get(eventType) <= 0 || eventTypes.get(eventType) >= 1) {
                System.err.println("ERROR! Probability less than 0 or greater than 1");
            }
        }
    }

    public ArrayList<EventType> choose() {
        ArrayList<EventType> result = new ArrayList<EventType>();
        for (EventType eventType : eventTypes.keySet()) {
             if (Math.random() < eventTypes.get(eventType)) {
                 result.add(eventType);
             }
        }
        return result;
    }

    public static void main(String[] args) {
        HashMap<EventType, Double> eventTypes = new HashMap<>();
        eventTypes.put(EventType.DEP1, 0.5);
        eventTypes.put(EventType.DEP2, 0.5);
        eventTypes.put(EventType.DEP3, 0.5);
        eventTypes.put(EventType.DEP4, 0.5);
        eventTypes.put(EventType.DEP5, 0.5);

        RandomChooserForCustomer chooser = new RandomChooserForCustomer(eventTypes);

        for (int i = 0; i < 10; i++) {
            System.out.println(chooser.choose());
        }
    }
}
