package controller;

import framework.Trace;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.canvas.Canvas;
import model.*;

public class SimulatorController {
    MyEngine myEngine;
    Router[] routers;
    ServicePoint[] servicePoints;

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
    private Label currentTime;

    @FXML
    private TextField simulationTime;

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
        myEngine.getClock().setClock(0);

        // setMyEngineParameters();
        routers = myEngine.getRouters();
        servicePoints = myEngine.getServicePoints();
        runningLabel.setText("Preparing");

        // Set UI
        pauseSimulationButton.setDisable(true);
        reloadButton.setDisable(true);
        startSimulationButton.setDisable(false);
        setFormDisable(false);

        // Set Time
        updateUI();
        simulationTime.setText("1000");
    }

    @FXML
    private void startSimulationButtonClicked() {

        pauseSimulationButton.setDisable(false);
        startSimulationButton.setDisable(true);

        if (myEngine.getPauseStatus()) {
            myEngine.resume();
            // disable UI
            reloadButton.setDisable(true);
        }
        runningLabel.setText("Running...");
        setFormDisable(true);

        Thread thread = new Thread(() -> {

            System.out.println("startSimulationButtonClicked() called");

            Trace.setTraceLevel(Trace.Level.INFO);

            myEngine.setSimulationTime(Double.parseDouble(simulationTime.getText()));

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

        setFormDisable(false);

        runningLabel.setText("Pausing...");
        startSimulationButton.setDisable(false);
    }

    @FXML
    private void reloadButtonClicked() {
        initialize();
    }

    // Run simulation with UI
    private void runSimulation() throws InterruptedException {
        myEngine.initialize();
        while (myEngine.simulate()) {

            Thread.sleep(1);

            System.out.println("This time is " + myEngine.getClock().getClock());

            Trace.out(Trace.Level.INFO, "\nA-phase: time is " + myEngine.currentTime());
            myEngine.getClock().setClock(myEngine.currentTime());

            Trace.out(Trace.Level.INFO, "\nB-phase:");
            runBEventsWithUI();

            Trace.out(Trace.Level.INFO, "\nC-phase:");
            myEngine.tryCEvents();

            updateUI();
        }
        reloadButton.setDisable(false);
        pauseSimulationButton.setDisable(true);

        myEngine.results();
    }

    private void runBEventsWithUI() {
        while (myEngine.getEventList().getNextEventTime() == myEngine.getClock().getClock()) {
            myEngine.runEvent(myEngine.getEventList().remove());
        }
    }

    public void updateUI() {
        Platform.runLater(() -> {

            Label[][] counters = {
                {refuellingCustomer, refuellingCustomerServed},
                {washingCustomer, washingCustomerServed},
                {shoppingCustomer, shoppingCustomerServed},
                {payingCustomer, payingCustomerServed},
                {dryingCustomer, dryingCustomerServed}
            };

            for (int i = 0; i < counters.length; i++) {
                counters[i][0].setText(
                    String.valueOf(servicePoints[i].getQueueSize())
                );
                counters[i][1].setText(
                    String.valueOf(
                        servicePoints[i].getNumberOfServedCustomer()
                    )
                );
            }
            arrivedCustomer.setText(String.valueOf(routers[0].getNumberOfArrivedCustomer()));
            exitCustomer.setText(String.valueOf(routers[2].getNumberOfArrivedCustomer()));

            currentTime.setText(String.format("%.2f", myEngine.getClock().getClock()));
        });
    }

    public void setSpinners() {
        Spinner[] spinners = new Spinner[] {
            arriveMain, arriveVariance, refuelMain, refuelVariance, washMain,
            washVariance, shoppingMain, shoppingVariance, payingMain,
            payingVariance, dryingMain, dryingVariance
        };
        SpinnerValueFactory<Double>[] factorySpinners = new SpinnerValueFactory[] {
            arriveMF, arriveVF, refuelMF, refuelVF, washMF, washVF,
            shoppingMF, shoppingVF, payingMF, payingVF, dryingMF, dryingVF
        };

        for (int i = 0; i < spinners.length; i++) {
            spinners[i].setValueFactory(factorySpinners[i]);
            spinners[i].setEditable(true);
        }
    }

    public void setFormDisable(boolean disable) {
        Spinner[] spinners = new Spinner[] {
            arriveMain, arriveVariance, refuelMain, refuelVariance, washMain,
            washVariance, shoppingMain, shoppingVariance, payingMain,
            payingVariance, dryingMain, dryingVariance
        };

        for (Spinner spinner : spinners) {
            spinner.setDisable(disable);
        }

        simulationTime.setDisable(disable);


    }
}
