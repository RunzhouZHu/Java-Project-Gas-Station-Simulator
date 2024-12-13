package view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class SimulatorViewTest extends ApplicationTest {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        SimulatorView simulatorView = new SimulatorView();
        simulatorView.start(stage);
    }

    @Test
    void testStart() {
        Scene scene = primaryStage.getScene();
        assertNotNull(scene, "Scene should not be null");
        assertEquals("Simulator", primaryStage.getTitle(), "Stage title should be 'Simulator'");
    }
}