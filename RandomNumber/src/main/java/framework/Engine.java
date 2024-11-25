package framework;

public class Engine {
    //time when the simulation will be stopped
    private double simulationTime = 0;

    // to simplify the code (clock.getClock() instead Clock.getInstance().getClock())
    private Clock clock;

    // events to be processed are stored here
    protected EventList eventList;

    public void run() {
        initialize(); // creating, e.g., the first event

        while (simulate()) {
            Trace.out(Trace.Level.INFO, "\nA-phase: time is " + currentTime());
            clock.setClock(currentTime());

            Trace.out(Trace.Level.INFO, "\nB-phase:");
            runBEvents();

            Trace.out(Trace.Level.INFO, "\nC-phase:");
            tryCEvents();

        }

        results();
    }

    private void runBEvents() {
        while (eventList.getNextEventTime() == clock.getClock()) {
            runEvent(eventList.remove());
        }
    }

    private double currentTime() {
        return eventList.getNextEventTime();
    }

    private boolean simulate() {
        return clock.getClock() < eventList.getNextEventTime();
    }


    // Defined in model-package's class who is inheriting the Engine class
    protected void runEvent(Event event) {}
    protected void tryCEvents() {}
    protected void initialize() {}
    protected void results() {}
}
