package controller;

import framework.Trace;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Controller class for the simulator. Manages the UI components and the simulation engine.
 */
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
    private final SpinnerValueFactory<Double> arriveMF = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 10, 7);
    @FXML
    private Spinner<Double> arriveVariance;
    private final SpinnerValueFactory<Double> arriveVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 10, 5);
    @FXML
    private Spinner<Double> refuelMain;
    private final SpinnerValueFactory<Double> refuelMF = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 50, 50);
    @FXML
    private Spinner<Double> refuelVariance;
    private final SpinnerValueFactory<Double> refuelVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 10, 5);
    @FXML
    private Spinner<Double> washMain;
    private final SpinnerValueFactory<Double> washMF = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 40, 40);
    @FXML
    private Spinner<Double> washVariance;
    private final SpinnerValueFactory<Double> washVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 10, 5);
    @FXML
    private Spinner<Double> shoppingMain;
    private final SpinnerValueFactory<Double> shoppingMF = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 50, 50);
    @FXML
    private Spinner<Double> shoppingVariance;
    private final SpinnerValueFactory<Double> shoppingVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 10, 5);
    @FXML
    private Spinner<Double> dryingMain;
    private final SpinnerValueFactory<Double> dryingMF = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 40, 40);
    @FXML
    private Spinner<Double> dryingVariance;
    private final SpinnerValueFactory<Double> dryingVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 10, 1);
    @FXML
    private Spinner<Double> payingMain;
    private final SpinnerValueFactory<Double> payingMF = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 50, 50);
    @FXML
    private Spinner<Double> payingVariance;
    private final SpinnerValueFactory<Double> payingVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 10, 5);


    @FXML
    private Canvas mainCanvas;

    @FXML
    private Label runningLabel;

    @FXML
    private Label currentTime;

    @FXML
    private TextField simulationTime;

    //@FXML
    //private Spinner<Double> delay;
    //private final SpinnerValueFactory<Double> delayDefault = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 20, 1);

    @FXML
    private Slider delay;


    /**
     * Initializes the controller. Sets up the spinners, engine, and UI components.
     */
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
                arriveVariance.getValue(),
                delay.getValue()
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
        if (Double.parseDouble(simulationTime.getText()) <= 0) {
            simulationTime.setText("1000");
        }
    }
    /**
     * Handles the start simulation button click event. Starts or resumes the simulation.
     */
    @FXML
    private void startSimulationButtonClicked() {
        delay.valueProperty().addListener((observable, oldValue, newValue) -> {
            //label.setText("Current value: " + newValue.intValue());
            myEngine.setDelay(newValue.intValue());
        });


        if (myEngine.getPauseStatus()) {
            myEngine.resume();
            // disable UI
            myEngine.setDelay(delay.getValue());
            reloadButton.setDisable(true);
        } else {
            initialize();
        }

        pauseSimulationButton.setDisable(false);
        startSimulationButton.setDisable(true);

        runningLabel.setText("Running...");
        setFormDisable(true);

        GraphicsContext gc = mainCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());

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

        Thread carThread = new Thread(() -> {
            while (true) {
                try {
                    Car car = myEngine.getCarQueue().take();
                    while (car.getMoving()) {
                        car.move();
                        Platform.runLater(() -> drawCar(gc, car));
                        Thread.sleep(myEngine.getDelay());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        carThread.start();

    }

    public void drawCar(GraphicsContext gc, Car car) {
        gc.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
        Image carImage = new Image(car.getIcon());
        gc.drawImage(carImage, car.getX(), car.getY(), car.getWidth(), car.getHeight());
    }
    /**
     * Handles the pause simulation button click event. Pauses the simulation.
     */
    @FXML
    private void pauseSimulationButtonClicked() {
        myEngine.pause();

        pauseSimulationButton.setDisable(true);

        reloadButton.setDisable(false);

        setFormDisable(false);

        runningLabel.setText("Pausing...");
        startSimulationButton.setDisable(false);
    }
    /**
     * Handles the reload button click event. Reinitializes the simulation.
     */
    @FXML
    private void reloadButtonClicked() {
        initialize();
    }

    // Run simulation with UI
    /**
     * Runs the simulation with UI updates.
     * @throws InterruptedException if the simulation thread is interrupted
     */
    private void runSimulation() throws InterruptedException {
        myEngine.initialize();
        while (!myEngine.simulateDone()) {
        //while (true) {

            Thread.sleep(myEngine.getDelay()*20);

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
    /**
     * Runs B-phase events with UI updates.
     */
    private void runBEventsWithUI() {
        while (myEngine.getEventList().getNextEventTime() == myEngine.getClock().getClock()) {
            myEngine.runEvent(myEngine.getEventList().remove());
        }
    }
    /**
     * Updates the UI components with the current simulation state.
     */
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
            arrivedCustomer.setText(String.valueOf(myEngine.getArrivedCount()));

            currentTime.setText(String.format("%.2f", myEngine.getClock().getClock()));
        });
    }
    /**
     * Sets up the spinners with their value factories.
     */
    public void setSpinners() {
        Spinner[] spinners = new Spinner[] {
            arriveMain, arriveVariance, refuelMain, refuelVariance, washMain,
            washVariance, shoppingMain, shoppingVariance, payingMain,
            payingVariance, dryingMain, dryingVariance//, delay
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
    /**
     * Enables or disables the form components.
     * @param disable true to disable the form components, false to enable them
     */
    public void setFormDisable(boolean disable) {
        Spinner[] spinners = new Spinner[] {
            arriveMain, arriveVariance, refuelMain, refuelVariance, washMain,
            washVariance, shoppingMain, shoppingVariance, payingMain,
            payingVariance, dryingMain, dryingVariance//, delay
        };

        for (Spinner spinner : spinners) {
            spinner.setDisable(disable);
        }

        simulationTime.setDisable(disable);


    }
    public Button getStartSimulationButton() {
        return startSimulationButton;
    }

    public Button getPauseSimulationButton() {
        return pauseSimulationButton;
    }

    public Button getReloadButton() {
        return reloadButton;
    }

    public Label getArrivedCustomer() {
        return arrivedCustomer;
    }

    public Label getRefuellingCustomer() {
        return refuellingCustomer;
    }

    public Label getWashingCustomer() {
        return washingCustomer;
    }

    public Label getDryingCustomer() {
        return dryingCustomer;
    }

    public Label getPayingCustomer() {
        return payingCustomer;
    }

    public Label getShoppingCustomer() {
        return shoppingCustomer;
    }

    public Label getRefuellingCustomerServed() {
        return refuellingCustomerServed;
    }

    public Label getWashingCustomerServed() {
        return washingCustomerServed;
    }

    public Label getDryingCustomerServed() {
        return dryingCustomerServed;
    }

    public Label getPayingCustomerServed() {
        return payingCustomerServed;
    }

    public Label getShoppingCustomerServed() {
        return shoppingCustomerServed;
    }

    public Spinner<Double> getArriveMain() {
        return arriveMain;
    }

    public Spinner<Double> getArriveVariance() {
        return arriveVariance;
    }

    public Spinner<Double> getRefuelMain() {
        return refuelMain;
    }

    public Spinner<Double> getRefuelVariance() {
        return refuelVariance;
    }

    public Spinner<Double> getWashMain() {
        return washMain;
    }

    public Spinner<Double> getWashVariance() {
        return washVariance;
    }

    public Spinner<Double> getShoppingMain() {
        return shoppingMain;
    }

    public Spinner<Double> getShoppingVariance() {
        return shoppingVariance;
    }

    public Spinner<Double> getDryingMain() {
        return dryingMain;
    }

    public Spinner<Double> getDryingVariance() {
        return dryingVariance;
    }

    public Spinner<Double> getPayingMain() {
        return payingMain;
    }

    public Spinner<Double> getPayingVariance() {
        return payingVariance;
    }

    public Canvas getMainCanvas() {
        return mainCanvas;
    }

    public Label getRunningLabel() {
        return runningLabel;
    }

    public Label getCurrentTime() {
        return currentTime;
    }

    public TextField getSimulationTime() {
        return simulationTime;
    }

    public Slider getDelay() {
        return delay;
    }
}
