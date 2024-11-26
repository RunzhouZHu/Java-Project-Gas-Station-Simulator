import framework.Engine;
import framework.Trace;
import model.MyEngine;

public class Simulator {
    public static void main(String[] args) {
        Trace.setTraceLevel(Trace.Level.INFO);

        Engine m = new MyEngine();
        m.setSimulationTime(100);
        m.run();
    }
}
