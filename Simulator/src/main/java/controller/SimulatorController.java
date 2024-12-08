package controller;

import framework.Event;
import framework.Trace;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import model.*;

public class SimulatorController {
    static MyEngine myEngine = new MyEngine();

    Router[] routers = myEngine.getRouters();
    ServicePoint[] servicePoints = myEngine.getServicePoints();

    MyEngineController mEC = new MyEngineController();

    // Buttons
    @FXML
    private Button startSimulationButton;

    @FXML
    private Button pauseSimulationButton;

    // Labels
    @FXML
    private Label arrivedCustomer;

    @FXML
    private Label refuellingCustomer;

    @FXML
    private Label washingCustomer;

    @FXML
    private Label dryingCustomer;

    @FXML
    private Label payingCustomer;

    @FXML
    private Label shoppingCustomer;

    @FXML
    private Label refuellingCustomerServed;

    @FXML
    private Label washingCustomerServed;

    @FXML
    private Label dryingCustomerServed;

    @FXML
    private Label payingCustomerServed;

    @FXML
    private Label shoppingCustomerServed;

    //
    @FXML
    private Label exitCustomer;

    @FXML
    private Canvas mainCanvas;

    @FXML
    private void startSimulationButtonClicked() {

        if (myEngine.getPauseStatus()) {
            myEngine.resume();
            pauseSimulationButton.setDisable(false);
        }

        Thread thread = new Thread(() -> {

            System.out.println("startSimulationButtonClicked() called");

            Trace.setTraceLevel(Trace.Level.INFO);

            myEngine.setSimulationTime(1000);

            try {
                runSimulation();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });

        thread.start();

    }

    @FXML
    private void pauseSimulationButtonClicked() {
        myEngine.pause();
        pauseSimulationButton.setDisable(true);
    }

    // Run simulation with UI
    private void runSimulation() throws InterruptedException {
        myEngine.initialize();
        while (myEngine.simulate()) {

            Thread.sleep(30);

            Trace.out(Trace.Level.INFO, "\nA-phase: time is " + myEngine.currentTime());
            myEngine.getClock().setClock(myEngine.currentTime());

            Trace.out(Trace.Level.INFO, "\nB-phase:");
            runBEventsWithUI();

            Trace.out(Trace.Level.INFO, "\nC-phase:");
            mEC.tryCEventsWithUI(myEngine);

            updateUI();
        }

        myEngine.results();
    }

    private void runBEventsWithUI() {
        while (myEngine.getEventList().getNextEventTime() == myEngine.getClock().getClock()) {
            mEC.runEventWithUI(myEngine.getEventList().remove(), routers, servicePoints, myEngine);
        }
    }

    public void updateUI() {
        Platform.runLater(() -> {
            refuellingCustomer.setText(String.valueOf(servicePoints[0].getQueueSize()));
            washingCustomer.setText(String.valueOf(servicePoints[1].getQueueSize()));
            shoppingCustomer.setText(String.valueOf(servicePoints[2].getQueueSize()));
            payingCustomer.setText(String.valueOf(servicePoints[3].getQueueSize()));
            dryingCustomer.setText(String.valueOf(servicePoints[4].getQueueSize()));

            refuellingCustomerServed.setText(String.valueOf(servicePoints[0].getNumberOfServedCustomer()));
            washingCustomerServed.setText(String.valueOf(servicePoints[1].getNumberOfServedCustomer()));
            shoppingCustomerServed.setText(String.valueOf(servicePoints[2].getNumberOfServedCustomer()));
            payingCustomerServed.setText(String.valueOf(servicePoints[3].getNumberOfServedCustomer()));
            dryingCustomerServed.setText(String.valueOf(servicePoints[4].getNumberOfServedCustomer()));

            arrivedCustomer.setText(String.valueOf(routers[0].getNumberOfArrivedCustomer()));
            exitCustomer.setText(String.valueOf(routers[2].getNumberOfArrivedCustomer()));
        });
    }
}
