package framework;

import java.util.PriorityQueue;

public class EventList {
    private PriorityQueue<Event> eventlist;

    public EventList() {
        eventlist = new PriorityQueue<>();
    }

    public Event remove() {
        Trace.out(Trace.Level.INFO, "Removing from the event list " + eventlist.peek().getEventType() + " " + eventlist.peek().getTime());
        return eventlist.remove();
    }

    public void add(Event event) {
        Trace.out(Trace.Level.INFO, "Adding to the event list " + event.getEventType() + " " + event.getTime());
        eventlist.add(event);
    }

    public double getNextEventTime() {
        return eventlist.peek().getTime();
    }
}
