package framework;
/**
 * The Engine class manages the simulation, including the clock, event list, and simulation control.
 */
public class Engine {
    //time when the simulation will be stopped
    private double simulationTime = 0;

    // to simplify the code (clock.getClock() instead Clock.getInstance().getClock())
    private Clock clock;

    // events to be processed are stored here
    protected EventList eventList;

    // Pause simulation button will control this
    private boolean pause = false;
    /**
     * Constructs an Engine and initializes the clock and event list.
     */
    public Engine() {
        clock = Clock.getInstance();

        eventList = new EventList();
    }
    /**
     * Sets the simulation time.
     *
     * @param simulationTime the time to set for the simulation
     */
    public void setSimulationTime(double simulationTime) {
        this.simulationTime = simulationTime;
    }
    /**
     * Runs the simulation by initializing and processing events.
     */
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
    /**
     * Runs B-phase events.
     */
    private void runBEvents() {
        while (eventList.getNextEventTime() <= clock.getClock()) {
            runEvent(eventList.remove());
        }
    }
    /**
     * Returns the current time of the simulation.
     *
     * @return the current time of the simulation
     */
    public double currentTime() {
        return eventList.getNextEventTime();
    }
    /**
     * Simulates the events until the simulation time is reached or paused.
     *
     * @return true if the simulation should continue, false otherwise
     */
    public boolean simulate() {
        return clock.getClock() < simulationTime && !pause;
    }

    /**
     * Pauses the simulation.
     */
    public void pause() {
        pause = true;
    }
    /**
     * Resumes the simulation.
     */
    public void resume() {
        pause = false;
    }
    /**
     * Returns the pause status of the simulation.
     *
     * @return true if the simulation is paused, false otherwise
     */
    public boolean getPauseStatus() {
        return pause;
    }
    /**
     * Returns the clock instance.
     *
     * @return the clock instance
     */
    public Clock getClock() {
        return clock;
    }
    /**
     * Returns the event list.
     *
     * @return the event list
     */
    public EventList getEventList() {
        return eventList;
    }


    // Defined in model-package's class who is inheriting the Engine class
    /**
     * Processes the given event.
     *
     * @param event the event to process
     */
    protected void runEvent(Event event) {}
    /**
     * Attempts to run C-phase events.
     */
    protected void tryCEvents() {}
    /**
     * Initializes the simulation, setting up initial events and states.
     */
    protected void initialize() {}
    /**
     * Collects and processes the results of the simulation.
     */
    protected void results() {}
}
