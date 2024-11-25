package framework;

public class Event implements Comparable<Event> {
    private IEventType eventType;
    private double time;

    public Event(IEventType eventType, double time) {
        this.eventType = eventType;
        this.time = time;
    }

    public void setEventType(IEventType eventType) {
        this.eventType = eventType;
    }
    public IEventType getEventType() {
        return eventType;
    }

    public void setTime(double time) {
        this.time = time;
    }
    public double getTime() {
        return time;
    }

    @Override
    public int compareTo(Event arg) {
        if(this.time < arg.time) return -1;
        else if(this.time > arg.time) return 1;
        return 0;
    }
}
