package controller;

import framework.Trace;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import model.*;

public class SimulatorController {
    MyEngine myEngine;
    Router[] routers;
    ServicePoint[] servicePoints;

    MyEngineController mEC = new MyEngineController();

    // Buttons
    @FXML
    private Button startSimulationButton;

    @FXML
    private Button pauseSimulationButton;

    @FXML
    private Button reloadButton;

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

    @FXML
    private Spinner<Double> arriveMain;
    private final SpinnerValueFactory<Double> arriveMF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10, 8);
    @FXML
    private Spinner<Double> arriveVariance;
    private final SpinnerValueFactory<Double> arriveVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10, 1);
    @FXML
    private Spinner<Double> refuelMain;
    private final SpinnerValueFactory<Double> refuelMF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 50, 20);
    @FXML
    private Spinner<Double> refuelVariance;
    private final SpinnerValueFactory<Double> refuelVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10, 5);
    @FXML
    private Spinner<Double> washMain;
    private final SpinnerValueFactory<Double> washMF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 50, 20);
    @FXML
    private Spinner<Double> washVariance;
    private final SpinnerValueFactory<Double> washVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10, 5);
    @FXML
    private Spinner<Double> shoppingMain;
    private final SpinnerValueFactory<Double> shoppingMF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 50, 20);
    @FXML
    private Spinner<Double> shoppingVariance;
    private final SpinnerValueFactory<Double> shoppingVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10, 5);
    @FXML
    private Spinner<Double> dryingMain;
    private final SpinnerValueFactory<Double> dryingMF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 50, 20);
    @FXML
    private Spinner<Double> dryingVariance;
    private final SpinnerValueFactory<Double> dryingVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10, 5);
    @FXML
    private Spinner<Double> payingMain;
    private final SpinnerValueFactory<Double> payingMF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 50, 20);
    @FXML
    private Spinner<Double> payingVariance;
    private final SpinnerValueFactory<Double> payingVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10, 5);

    //
    @FXML
    private Label exitCustomer;

    @FXML
    private Canvas mainCanvas;

    @FXML
    private Label runningLabel;

    @FXML
    private void initialize() {
        setSpinners();
        myEngine = new MyEngine(
                refuelMain.getValue(),
                refuelVariance.getValue(),
                washMain.getValue(),
                washVariance.getValue(),
                shoppingMain.getValue(),
                shoppingVariance.getValue(),
                payingMain.getValue(),
                payingVariance.getValue(),
                dryingMain.getValue(),
                dryingVariance.getValue(),
                arriveMain.getValue(),
                arriveVariance.getValue()
        );
        // setMyEngineParameters();
        routers = myEngine.getRouters();
        servicePoints = myEngine.getServicePoints();
        runningLabel.setText("Preparing");

        // Set UI
        pauseSimulationButton.setDisable(true);
        reloadButton.setDisable(true);
    }

    @FXML
    private void startSimulationButtonClicked() {

        pauseSimulationButton.setDisable(false);
        startSimulationButton.setDisable(true);

        if (myEngine.getPauseStatus()) {
            myEngine.resume();
            // disable UI
            reloadButton.setDisable(true);

            arriveMain.setDisable(true);
            arriveVariance.setDisable(true);
            refuelMain.setDisable(true);
            refuelVariance.setDisable(true);
            washMain.setDisable(true);
            washVariance.setDisable(true);
            shoppingMain.setDisable(true);
            shoppingVariance.setDisable(true);
            payingMain.setDisable(true);
            payingVariance.setDisable(true);
            dryingMain.setDisable(true);
            dryingVariance.setDisable(true);

            runningLabel.setText("Running...");
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

        reloadButton.setDisable(false);
        arriveMain.setDisable(false);
        arriveVariance.setDisable(false);
        refuelMain.setDisable(false);
        refuelVariance.setDisable(false);
        washMain.setDisable(false);
        washVariance.setDisable(false);
        shoppingMain.setDisable(false);
        shoppingVariance.setDisable(false);
        payingMain.setDisable(false);
        payingVariance.setDisable(false);
        dryingMain.setDisable(false);
        dryingVariance.setDisable(false);

        runningLabel.setText("Pausing...");
        startSimulationButton.setDisable(false);
    }

    @FXML
    private void reloadButtonClicked() {
        myEngine = new MyEngine(
                refuelMain.getValue(),
                refuelVariance.getValue(),
                washMain.getValue(),
                washVariance.getValue(),
                shoppingMain.getValue(),
                shoppingVariance.getValue(),
                payingMain.getValue(),
                payingVariance.getValue(),
                dryingMain.getValue(),
                dryingVariance.getValue(),
                arriveMain.getValue(),
                arriveVariance.getValue()
        );
        myEngine.pause();
        routers = myEngine.getRouters();
        servicePoints = myEngine.getServicePoints();

        updateUI();
        runningLabel.setText("Preparing...");
        reloadButton.setDisable(true);
        startSimulationButton.setDisable(false);
    }

    // Run simulation with UI
    private void runSimulation() throws InterruptedException {
        myEngine.initialize();
        while (myEngine.simulate()) {

            Thread.sleep(30);

            System.out.println("This time is " + myEngine.getClock().getClock());

            Trace.out(Trace.Level.INFO, "\nA-phase: time is " + myEngine.currentTime());

            Trace.out(Trace.Level.INFO, "\nB-phase:");
            runBEventsWithUI();

            Trace.out(Trace.Level.INFO, "\nC-phase:");
            mEC.tryCEventsWithUI(myEngine);

            updateUI();

            myEngine.getClock().gotoNextMoment();
        }

        myEngine.results();
    }

    private void runBEventsWithUI() {
        while (myEngine.getEventList().getNextEventTime() <= myEngine.getClock().getClock()) {
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

    public void setSpinners() {
        arriveMain.setValueFactory(arriveMF);
        arriveMain.setEditable(true);
        arriveVariance.setValueFactory(arriveVF);
        arriveVariance.setEditable(true);
        refuelMain.setValueFactory(refuelMF);
        refuelMain.setEditable(true);
        refuelVariance.setValueFactory(refuelVF);
        refuelVariance.setEditable(true);
        washMain.setValueFactory(washMF);
        washMain.setEditable(true);
        washVariance.setValueFactory(washVF);
        washVariance.setEditable(true);
        shoppingMain.setValueFactory(shoppingMF);
        shoppingMain.setEditable(true);
        shoppingVariance.setValueFactory(shoppingVF);
        shoppingVariance.setEditable(true);
        dryingMain.setValueFactory(dryingMF);
        dryingMain.setEditable(true);
        dryingVariance.setValueFactory(dryingVF);
        dryingVariance.setEditable(true);
        payingMain.setValueFactory(payingMF);
        payingMain.setEditable(true);
        payingVariance.setValueFactory(payingVF);
        payingVariance.setEditable(true);
    }
}
