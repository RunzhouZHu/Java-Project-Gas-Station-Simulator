package framework;

public class Engine {
    //time when the simulation will be stopped
    private double simulationTime = 0;

    // to simplify the code (clock.getClock() instead Clock.getInstance().getClock())
    private Clock clock;

    // events to be processed are stored here
    protected EventList eventList;

    // Pause simulation button will control this
    private boolean pause = false;

    public Engine() {
        clock = Clock.getInstance();

        eventList = new EventList();
    }

    public void setSimulationTime(double simulationTime) {
        this.simulationTime = simulationTime;
    }

    public void run() {
        initialize(); // creating, e.g., the first event

        /*
        while (simulate()) {
            Trace.out(Trace.Level.INFO, "\nA-phase: time is " + currentTime());
            clock.setClock(currentTime());

            Trace.out(Trace.Level.INFO, "\nB-phase:");
            runBEvents();

            Trace.out(Trace.Level.INFO, "\nC-phase:");
            tryCEvents();

        }

         */

        while (clock.getClock() < simulationTime) {
            Trace.out(Trace.Level.INFO, "\nA-phase: time is " + currentTime());
            // clock.setClock(currentTime());

            Trace.out(Trace.Level.INFO, "\nB-phase:");
            runBEvents();

            Trace.out(Trace.Level.INFO, "\nC-phase:");
            tryCEvents();

            clock.gotoNextMoment();
        }
        results();
    }

    private void runBEvents() {
        while (eventList.getNextEventTime() <= clock.getClock()) {
            runEvent(eventList.remove());
        }
    }

    public double currentTime() {
        return eventList.getNextEventTime();
    }

    public boolean simulate() {
        return clock.getClock() < simulationTime && !pause;
    }

    public void pause() {
        pause = true;
    }
    public void resume() {
        pause = false;
    }
    public boolean getPauseStatus() {
        return pause;
    }

    public Clock getClock() {
        return clock;
    }

    public EventList getEventList() {
        return eventList;
    }


    // Defined in model-package's class who is inheriting the Engine class
    protected void runEvent(Event event) {}
    protected void tryCEvents() {}
    protected void initialize() {}
    protected void results() {}
}
