package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
        Get next event type randomly according to the output possibility of
    the router.
        This class should be abandoned.
 */
public class RandomChooserForRouter {
    private final HashMap<EventType, Integer> eventTypes;
    private Integer totalProbability = 0;

    public RandomChooserForRouter(HashMap<EventType, Integer> eventTypes) {
        this.eventTypes = eventTypes;
        for (EventType eventType : eventTypes.keySet()) {
            int probability = eventTypes.get(eventType);
            eventTypes.put(eventType, probability + totalProbability);
            totalProbability += probability;
        }
    }

    public EventType choose() {
        Random rand = new Random();
        int randomValue = rand.nextInt(totalProbability);
        for (Map.Entry<EventType, Integer> entry : eventTypes.entrySet()) {
            if (randomValue < entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        HashMap<EventType, Integer> eventTypes = new HashMap<>();
        eventTypes.put(EventType.WASHING, 10);
        eventTypes.put(EventType.PAYING, 20);
        eventTypes.put(EventType.DRYING, 30);

        RandomChooserForRouter chooser = new RandomChooserForRouter(eventTypes);

        for (int i = 0; i < 10; i++) {
            System.out.println(chooser.choose());
        }
    }
}
