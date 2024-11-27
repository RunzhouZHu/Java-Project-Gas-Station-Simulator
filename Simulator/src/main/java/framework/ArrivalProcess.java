package framework;

import eduni.distributions.ContinuousGenerator;

public class ArrivalProcess {
    private ContinuousGenerator generator;
    private EventList eventList;
    private IEventType eventType;

    public ArrivalProcess(ContinuousGenerator generator, EventList eventList, IEventType eventType) {
        this.generator = generator;
        this.eventList = eventList;
        this.eventType = eventType;
    }

    public void generateNextEvent() {
        Event nextEvent = new Event(eventType, Clock.getInstance().getClock() + generator.sample());
        eventList.add(nextEvent);
    }
}
