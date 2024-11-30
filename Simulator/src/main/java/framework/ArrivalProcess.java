package framework;

import eduni.distributions.Normal;

public class ArrivalProcess {
    private Normal generator;
    private EventList eventList;
    private IEventType eventType;

    public ArrivalProcess(Normal generator, EventList eventList, IEventType eventType) {
        this.generator = generator;
        this.eventList = eventList;
        this.eventType = eventType;
    }

    public void generateNextEvent() {
        Event nextEvent = new Event(eventType, Clock.getInstance().getClock() + generator.sample());
        eventList.add(nextEvent);
    }
}
