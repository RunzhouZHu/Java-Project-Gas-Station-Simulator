package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class SimulatorControllerTest extends ApplicationTest {

    private SimulatorController controller;

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file and initialize the controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/simulator_view.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        // Set up the stage
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void testInitialize() {
        // Verify that the controller is initialized correctly
        assertNotNull(controller, "Controller should not be null");
        assertNotNull(controller.getStartSimulationButton(), "Start Simulation Button should not be null");
        assertNotNull(controller.getPauseSimulationButton(), "Pause Simulation Button should not be null");
        assertNotNull(controller.getReloadButton(), "Reload Button should not be null");
        assertNotNull(controller.getArrivedCustomer(), "Arrived Customer Label should not be null");
        assertNotNull(controller.getRefuellingCustomer(), "Refuelling Customer Label should not be null");
        assertNotNull(controller.getWashingCustomer(), "Washing Customer Label should not be null");
        assertNotNull(controller.getDryingCustomer(), "Drying Customer Label should not be null");
        assertNotNull(controller.getPayingCustomer(), "Paying Customer Label should not be null");
        assertNotNull(controller.getShoppingCustomer(), "Shopping Customer Label should not be null");
        assertNotNull(controller.getRefuellingCustomerServed(), "Refuelling Customer Served Label should not be null");
        assertNotNull(controller.getWashingCustomerServed(), "Washing Customer Served Label should not be null");
        assertNotNull(controller.getDryingCustomerServed(), "Drying Customer Served Label should not be null");
        assertNotNull(controller.getPayingCustomerServed(), "Paying Customer Served Label should not be null");
        assertNotNull(controller.getShoppingCustomerServed(), "Shopping Customer Served Label should not be null");
        assertNotNull(controller.getArriveMain(), "Arrive Main Spinner should not be null");
        assertNotNull(controller.getArriveVariance(), "Arrive Variance Spinner should not be null");
        assertNotNull(controller.getRefuelMain(), "Refuel Main Spinner should not be null");
        assertNotNull(controller.getRefuelVariance(), "Refuel Variance Spinner should not be null");
        assertNotNull(controller.getWashMain(), "Wash Main Spinner should not be null");
        assertNotNull(controller.getWashVariance(), "Wash Variance Spinner should not be null");
        assertNotNull(controller.getShoppingMain(), "Shopping Main Spinner should not be null");
        assertNotNull(controller.getShoppingVariance(), "Shopping Variance Spinner should not be null");
        assertNotNull(controller.getDryingMain(), "Drying Main Spinner should not be null");
        assertNotNull(controller.getDryingVariance(), "Drying Variance Spinner should not be null");
        assertNotNull(controller.getPayingMain(), "Paying Main Spinner should not be null");
        assertNotNull(controller.getPayingVariance(), "Paying Variance Spinner should not be null");
        assertNotNull(controller.getMainCanvas(), "Main Canvas should not be null");
        assertNotNull(controller.getRunningLabel(), "Running Label should not be null");
        assertNotNull(controller.getCurrentTime(), "Current Time Label should not be null");
        assertNotNull(controller.getSimulationTime(), "Simulation Time TextField should not be null");
        assertNotNull(controller.getDelay(), "Delay Slider should not be null");
    }

    @Test
    void testStartSimulationButtonClicked() {
        // Simulate button click
        clickOn(controller.getStartSimulationButton());

        // Verify that the simulation is started
        assertEquals("Running...", controller.getRunningLabel().getText(), "Running Label should display 'Running...'");
        assertFalse(controller.getPauseSimulationButton().isDisable(), "Pause Simulation Button should be enabled");
        assertTrue(controller.getStartSimulationButton().isDisable(), "Start Simulation Button should be disabled");
    }

    @Test
    void testPauseSimulationButtonClicked() {
        // Start the simulation first
        clickOn(controller.getStartSimulationButton());

        // Add a short delay to allow the UI to update
        sleep(500);

        // Simulate button click to pause the simulation
        clickOn(controller.getPauseSimulationButton());

        // Add a short delay to allow the UI to update
        sleep(500);

        // Verify that the simulation is paused
        assertEquals("Pausing...", controller.getRunningLabel().getText(), "Running Label should display 'Pausing...'");
        assertFalse(controller.getReloadButton().isDisable(), "Reload Button should be enabled");
        assertTrue(controller.getPauseSimulationButton().isDisable(), "Pause Simulation Button should be disabled");
    }

    @Test
    void testReloadButtonClicked() {
        // Simulate button click
        clickOn(controller.getReloadButton());

        // Verify that the simulation is reloaded
        assertEquals("Preparing", controller.getRunningLabel().getText(), "Running Label should display 'Preparing'");
        assertFalse(controller.getStartSimulationButton().isDisable(), "Start Simulation Button should be enabled");
        assertTrue(controller.getReloadButton().isDisable(), "Reload Button should be disabled");
    }
}