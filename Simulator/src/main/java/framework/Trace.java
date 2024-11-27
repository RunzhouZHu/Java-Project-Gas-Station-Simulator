package framework;

public class Trace {
    public enum Level {INFO, WARN, ERROR}
    private static Level traceLevel;

    public static void setTraceLevel(Level level) {
        traceLevel = level;
    }

    public static void out(Level level, String text) {
        if(level.ordinal() >= traceLevel.ordinal()) {
            System.out.println(text);
        }
    }
}
