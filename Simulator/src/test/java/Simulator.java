import framework.Engine;
import framework.Trace;
import model.MyEngine;

public class Simulator {
    public static void main(String[] args) {
        Trace.setTraceLevel(Trace.Level.INFO);

        Engine m = new MyEngine(
                20.0,
                5.0,
                20.0,
                5.0,
                20.0,
                5.0,
                20.0,
                5.0,
                20.0,
                5.0,
                5.0,
                5.0
        );
        m.setSimulationTime(1000);
        m.run();
    }
}
