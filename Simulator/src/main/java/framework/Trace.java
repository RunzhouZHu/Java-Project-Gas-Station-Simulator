package framework;
/**
 * The Trace class provides logging functionality for the simulation.
 * It allows setting a trace level and outputs messages based on the set level.
 */
public class Trace {
    /**
     * Enum representing the trace levels.
     */
    public enum Level {INFO, WARN, ERROR}
    private static Level traceLevel;

    /**
     * Sets the trace level.
     *
     * @param level the trace level to set
     */
    public static void setTraceLevel(Level level) {
        traceLevel = level;
    }

    /**
     * Outputs a message if the message level is greater than or equal to the current trace level.
     *
     * @param level the level of the message
     * @param text the message to output
     */
    public static void out(Level level, String text) {
        if(level.ordinal() >= traceLevel.ordinal()) {
            System.out.println(text);
        }
    }

    public static Level getTraceLevel() {
        return traceLevel;
    }
}
