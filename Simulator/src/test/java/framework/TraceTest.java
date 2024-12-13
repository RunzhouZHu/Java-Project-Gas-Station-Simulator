package framework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TraceTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testSetTraceLevel() {
        Trace.setTraceLevel(Trace.Level.INFO);
        assertEquals(Trace.Level.INFO, Trace.getTraceLevel(), "Trace level should be set to INFO");

        Trace.setTraceLevel(Trace.Level.WARN);
        assertEquals(Trace.Level.WARN,Trace.getTraceLevel(), "Trace level should be set to WARN");

        Trace.setTraceLevel(Trace.Level.ERROR);
        assertEquals(Trace.Level.ERROR, Trace.getTraceLevel(), "Trace level should be set to ERROR");
    }

    @Test
    void testOut() {
        Trace.setTraceLevel(Trace.Level.INFO);

        Trace.out(Trace.Level.INFO, "Info message");
        assertTrue(outContent.toString().contains("Info message"), "Output should contain 'Info message'");

        outContent.reset();
        Trace.out(Trace.Level.WARN, "Warn message");
        assertTrue(outContent.toString().contains("Warn message"), "Output should contain 'Warn message'");

        outContent.reset();
        Trace.out(Trace.Level.ERROR, "Error message");
        assertTrue(outContent.toString().contains("Error message"), "Output should contain 'Error message'");

        outContent.reset();
        Trace.setTraceLevel(Trace.Level.WARN);
        Trace.out(Trace.Level.INFO, "Info message");
        assertFalse(outContent.toString().contains("Info message"), "Output should not contain 'Info message'");

        outContent.reset();
        Trace.out(Trace.Level.WARN, "Warn message");
        assertTrue(outContent.toString().contains("Warn message"), "Output should contain 'Warn message'");

        outContent.reset();
        Trace.out(Trace.Level.ERROR, "Error message");
        assertTrue(outContent.toString().contains("Error message"), "Output should contain 'Error message'");
    }
}