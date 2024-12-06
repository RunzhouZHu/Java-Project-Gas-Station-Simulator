package framework;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Clock {
    private double clock;
    private static Clock instance;
    private final ScheduledExecutorService scheduler;
    private double timeStep;


    private Clock() {
        clock = 0;
        scheduler = Executors.newScheduledThreadPool(1);
        timeStep = 0.1;
        startClock();
    }

    public static Clock getInstance() {
        if (instance == null) {
            instance = new Clock();
        }
        return instance;
    }

    private void startClock() {
        scheduler.scheduleAtFixedRate(() -> {
            synchronized (this) {
                setClock(getClock() + timeStep);
            }
        }, 0, 1, TimeUnit.MILLISECONDS);
    }

    public synchronized void setTimeStep(double timeStep) {
        this.timeStep = timeStep;
    }

    public synchronized double getTimeStep() {
        return timeStep;
    }

    public void stopClock() {
        scheduler.shutdown();
    }

    public void setClock(double clock) {
        this.clock = clock;
    }

    public double getClock() {
        return clock;
    }
}
