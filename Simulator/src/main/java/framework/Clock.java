package framework;
/**
 * Singleton class representing a clock to keep track of simulation time.
 */
public class Clock {
	private double clock;
	private static Clock instance;
    /**
     * Private constructor to prevent instantiation.
     * Initializes the clock to 0.
     */
    private Clock() {
        clock = 0;
    }
    /**
     * Returns the singleton instance of the Clock.
     * If the instance does not exist, it creates one.
     *
     * @return the singleton instance of the Clock
     */
    public static Clock getInstance() {
        if (instance == null) {
            instance = new Clock();
        }
        return instance;
    }
    /**
     * Sets the current time of the clock.
     *
     * @param clock the current time to set
     */
    public void setClock(double clock) {
        this.clock = clock;
    }
    /**
     * Returns the current time of the clock.
     *
     * @return the current time of the clock
     */
    public double getClock() {
        return clock;
    }

}
