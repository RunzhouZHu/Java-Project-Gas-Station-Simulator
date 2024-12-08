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

    @FXML
    private Spinner<Double> arriveMain;
    @FXML
    private Spinner<Double> arriveVariance;
    @FXML
    private Spinner<Double> refuelMain;
    @FXML
    private Spinner<Double> refuelVariance;
    @FXML
    private Spinner<Double> washMain;
    @FXML
    private Spinner<Double> washVariance;
    @FXML
    private Spinner<Double> shoppingMain;
    @FXML
    private Spinner<Double> shoppingVariance;
    @FXML
    private Spinner<Double> dryingMain;
    @FXML
    private Spinner<Double> dryingVariance;
    @FXML
    private Spinner<Double> payingMain;
    @FXML
    private Spinner<Double> payingVariance;
    private final SpinnerValueFactory<Double> mainFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 50, 20);
    private SpinnerValueFactory<Double> varianceFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10, 5);

    //
    @FXML
    private Label exitCustomer;

    @FXML
    private Canvas mainCanvas;

    @FXML
    private void initialize() {
        arriveMain.setValueFactory(mainFactory);
        arriveMain.setEditable(true);
        arriveVariance.setValueFactory(varianceFactory);
        arriveVariance.setEditable(true);
        refuelMain.setValueFactory(mainFactory);
        refuelMain.setEditable(true);
        refuelVariance.setValueFactory(varianceFactory);
        refuelVariance.setEditable(true);
        washMain.setValueFactory(mainFactory);
        washMain.setEditable(true);
        washVariance.setValueFactory(varianceFactory);
        washVariance.setEditable(true);
        shoppingMain.setValueFactory(mainFactory);
        shoppingMain.setEditable(true);
        shoppingVariance.setValueFactory(varianceFactory);
        shoppingVariance.setEditable(true);
        dryingMain.setValueFactory(mainFactory);
        dryingMain.setEditable(true);
        dryingVariance.setValueFactory(varianceFactory);
        dryingVariance.setEditable(true);
        payingMain.setValueFactory(mainFactory);
        payingMain.setEditable(true);
        payingVariance.setValueFactory(varianceFactory);
        payingVariance.setEditable(true);



    }

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
