package framework;

import eduni.distributions.Normal;
/**
 * Class representing the arrival process of events.
 * Generates events based on a normal distribution.
 */
public class ArrivalProcess {
    private Normal generator;
    private EventList eventList;
    private IEventType eventType;
    /**
     * Constructs an ArrivalProcess with the specified generator, event list, and event type.
     *
     * @param generator the normal distribution generator for event times
     * @param eventList the list to which generated events will be added
     * @param eventType the type of events to generate
     */
    public ArrivalProcess(Normal generator, EventList eventList, IEventType eventType) {
        this.generator = generator;
        this.eventList = eventList;
        this.eventType = eventType;
    }
    /**
     * Generates the next event and adds it to the event list.
     */
    public void generateNextEvent() {
        Event nextEvent = new Event(eventType, Clock.getInstance().getClock() + generator.sample());
        eventList.add(nextEvent);
    }
}
