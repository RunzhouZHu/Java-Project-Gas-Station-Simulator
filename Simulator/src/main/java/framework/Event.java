package framework;
/**
 * Class representing an event in the simulation.
 * Implements Comparable to allow events to be sorted by time.
 */
public class Event implements Comparable<Event> {
    private IEventType eventType;
    private double time;

    /**
     * Constructs an Event with the specified event type and time.
     *
     * @param eventType the type of the event
     * @param time the time at which the event occurs
     */
    public Event(IEventType eventType, double time) {
        this.eventType = eventType;
        this.time = time;
    }

    /**
     * Sets the event type.
     *
     * @param eventType the type of the event
     */
    public void setEventType(IEventType eventType) {
        this.eventType = eventType;
    }

    /**
     * Returns the event type.
     *
     * @return the type of the event
     */
    public IEventType getEventType() {
        return eventType;
    }

    /**
     * Sets the time of the event.
     *
     * @param time the time at which the event occurs
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * Returns the time of the event.
     *
     * @return the time at which the event occurs
     */
    public double getTime() {
        return time;
    }

    /**
     * Compares this event with another event based on time.
     *
     * @param arg the event to compare to
     * @return -1 if this event occurs before the other event, 1 if it occurs after, 0 if they occur at the same time
     */
    @Override
    public int compareTo(Event arg) {
        if(this.time < arg.time) return -1;
        else if(this.time > arg.time) return 1;
        return 0;
    }
}
