package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Outputs the results of the simulation, including various performance metrics.
 */
public class RandomChooserForCustomer {
    private HashMap<EventType, Double> eventTypes;

    /**
     * Constructs a RandomChooserForCustomer with specified event types and their probabilities.
     *
     * @param eventTypes a map of event types and their corresponding probabilities
     */
    public RandomChooserForCustomer(HashMap<EventType, Double> eventTypes) {
        this.eventTypes = eventTypes;
        for (EventType eventType : eventTypes.keySet()) {
            if (eventTypes.get(eventType) <= 0 || eventTypes.get(eventType) >= 1) {
                System.err.println("ERROR! Probability less than 0 or greater than 1");
            }
        }
    }

    /**
     * Chooses event types for a customer based on the predefined probabilities.
     *
     * @return a list of chosen event types
     */
    public ArrayList<EventType> choose() {
        ArrayList<EventType> result = new ArrayList<EventType>();
        for (EventType eventType : eventTypes.keySet()) {
             if (Math.random() < eventTypes.get(eventType)) {
                 result.add(eventType);
             }
        }
        return result;
    }

    /**
     * Main method for testing the RandomChooserForCustomer class.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        HashMap<EventType, Double> eventTypes = new HashMap<>();
        eventTypes.put(EventType.REFUELLING, 0.5);
        eventTypes.put(EventType.WASHING,    0.5);
        eventTypes.put(EventType.SHOPPING,   0.5);
        eventTypes.put(EventType.PAYING,     0.5);
        eventTypes.put(EventType.DRYING,     0.5);

        RandomChooserForCustomer chooser = new RandomChooserForCustomer(eventTypes);

        for (int i = 0; i < 10; i++) {
            System.out.println(chooser.choose());
        }
    }
}
