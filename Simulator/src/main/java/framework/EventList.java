package framework;

import java.util.PriorityQueue;
/**
 * Class representing a list of events, managed as a priority queue.
 * Events are processed in the order of their scheduled times.
 */
public class EventList {
    private PriorityQueue<Event> eventlist;

    /**
     * Constructs an EventList and initializes the priority queue.
     */
    public EventList() {
        eventlist = new PriorityQueue<>();
    }

    /**
     * Removes and returns the event with the earliest scheduled time.
     *
     * @return the event with the earliest scheduled time
     */
    public Event remove() {
        Trace.out(Trace.Level.INFO, "Removing from the event list " + eventlist.peek().getEventType() + " " + eventlist.peek().getTime());
        return eventlist.remove();
    }

    /**
     * Adds an event to the event list.
     *
     * @param event the event to add
     */
    public void add(Event event) {
        Trace.out(Trace.Level.INFO, "Adding to the event list " + event.getEventType() + " " + event.getTime());
        eventlist.add(event);
    }

    /**
     * Returns the time of the next event to be processed.
     *
     * @return the time of the next event
     */
    public double getNextEventTime() {
        Event e = eventlist.peek();
        if (e == null) {
            return 0;
        }
        return e.getTime();
    }
}
