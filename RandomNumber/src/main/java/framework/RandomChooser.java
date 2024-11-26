package framework;

import model.EventType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomChooser {
    private final HashMap<EventType, Integer> eventTypes;
    private Integer totalProbability = 0;

    public RandomChooser(HashMap<EventType, Integer> eventTypes) {
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

            System.out.println(randomValue + " " + entry.getValue());

            if (randomValue < entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        HashMap<EventType, Integer> eventTypes = new HashMap<>();
        eventTypes.put(EventType.DEP1, 10);
        eventTypes.put(EventType.DEP2, 20);
        eventTypes.put(EventType.DEP3, 30);

        RandomChooser chooser = new RandomChooser(eventTypes);

        for (int i = 0; i < 10; i++) {
            System.out.println(chooser.choose());
        }
    }
}
