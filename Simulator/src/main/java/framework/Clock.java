package framework;

public class Clock {
    private double clock;
    private static Clock instance;

    private double timeStep = 0.1;

    private Clock() {
        clock = 0;
    }

    public static Clock getInstance() {
        if (instance == null) {
            instance = new Clock();
        }
        return instance;
    }

    public void setClock(double clock) {
        this.clock = clock;
    }

    public double getClock() {
        return clock;
    }

    public void gotoNextMoment() {
        clock += timeStep;
    }

    public void setTimeStep(double timeStep) {
        this.timeStep = timeStep;
    }
}
