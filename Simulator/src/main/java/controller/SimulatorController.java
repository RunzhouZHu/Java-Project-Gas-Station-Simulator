package controller;

import framework.Clock;
import framework.Event;
import framework.Trace;
import controller.CarController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import model.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;

import static java.lang.Integer.parseInt;

public class SimulatorController {
    static MyEngine myEngine = new MyEngine();

    Router[] routers = myEngine.getRouters();
    ServicePoint[] servicePoints = myEngine.getServicePoints();

    @FXML
    private Button startSimulationButton;

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
    private Label exitCustomer;

    @FXML
    private Canvas mainCanvas;

    @FXML
    private void startSimulationButtonClicked() {

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
    }

    private void counterUp(Label counter) {
        Platform.runLater(
            () -> counter.setText(
                String.valueOf(parseInt(counter.getText()) + 1)
            )
        );
    }

    private void counterDown(Label counter) {
        Platform.runLater(
            () -> counter.setText(
                String.valueOf(parseInt(counter.getText()) - 1)
            )
        );
    }
    
    private void driveCar(CarController carController) {
        Platform.runLater(
            () -> {
                GraphicsContext gc = mainCanvas.getGraphicsContext2D();
                gc.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
                Image carImage = new Image(carController.getCarIcon());
                gc.drawImage(
                    carImage,
                    carController.getCarX(),
                    carController.getCarY(),
                    carController.getCarWidth(),
                    carController.getCarHeight()
                );
            }
        );
    }

    // Run simulation with UI
    private void runSimulation() throws InterruptedException {
        myEngine.initialize();

        /*
        while (myEngine.simulate()) {

            Thread.sleep(30);

            Trace.out(Trace.Level.INFO, "\nA-phase: time is " + myEngine.currentTime());
            myEngine.getClock().setClock(myEngine.currentTime());

            Trace.out(Trace.Level.INFO, "\nB-phase:");
            runBEventsWithUI();

            Trace.out(Trace.Level.INFO, "\nC-phase:");
            tryCEventsWithUI();

            updateUI();
        }

         */

        while (myEngine.simulate()) {

            Thread.sleep(30);

            Trace.out(Trace.Level.INFO, "\nA-phase: time is " + myEngine.currentTime());
            myEngine.getClock().setClock(myEngine.currentTime());

            Trace.out(Trace.Level.INFO, "\nB-phase:");
            runBEventsWithUI();

            Trace.out(Trace.Level.INFO, "\nC-phase:");
            tryCEventsWithUI();

            updateUI();
        }

        myEngine.results();
    }

    private void runBEventsWithUI() {
        while (myEngine.getEventList().getNextEventTime() == myEngine.getClock().getClock()) {
            runEventWithUI(myEngine.getEventList().remove());
        }
    }

    private void runEventWithUI(Event event) {

        Customer customer;

        switch ((EventType) event.getEventType()) {
            case ARRIVE:
                customer = new Customer();

                CarController carController = customer.getCarController();
                driveCar(carController);
                carController.turnRight();

                new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        carController.carMove();
                        driveCar(carController);
                    }
                }.start();

                customer.setEventTypesToVisit(); // Random
                System.out.println("!!!!!New customer arrives: Customer" + customer.getId() + ", want service " + customer.getEventTypesToVisit());

                routers[0].addQueue(customer);

                counterUp(arrivedCustomer);

                myEngine.getArrivalProcesses().generateNextEvent();
                break;

            // Router events, the 'splits' should be at here
            case Rot1:
                customer = routers[0].removeQueue();

                System.out.println("!!!Customer " + customer.getId() + " leaving router1 and go to " + customer.getEventTypesToVisit().get(0));

                switch (customer.getEventTypesToVisit().get(0)) {
                    case REFUELLING:
                        servicePoints[0].addQueue(customer);

                        customer.getCarController().setCarTarget(250, 415);

                        break;
                    case WASHING, DRYING:
                        servicePoints[1].addQueue(customer);

                        break;
                    case SHOPPING:
                        servicePoints[2].addQueue(customer);

                        break;
                    case PAYING:
                        servicePoints[3].addQueue(customer);
                        break;
                }
                break;

            case Rot2:
                customer = routers[1].removeQueue();
                if (customer.getEventTypesToVisit().contains(EventType.DRYING)) {
                    servicePoints[4].addQueue(customer);

                    System.out.println("!!!Customer " + customer.getId() + " leaving router2 and go to DRYING.");

                } else {
                    routers[2].addQueue(customer);

                    System.out.println("!!!Customer " + customer.getId() + " leaving router2 and go to router3.");

                }
                break;

            case Rot3:
                customer = routers[2].removeQueue();
                if (customer.getEventTypesToVisit().isEmpty()) {
                    customer.getEventTypesToVisit().add(EventType.PAYING);
                    servicePoints[3].addQueue(customer);

                } else {
                    routers[0].addQueue(customer);

                    System.out.println("!!!Customer " + customer.getId() + " leaving router3 and reenter the system.");

                }
                break;

            // ServicePoint events
            case REFUELLING:
                customer = servicePoints[0].removeQueue();
                customer.getCarController().setCarTarget(270, 280);
                myEngine.doService(customer, EventType.REFUELLING, 2);
                customer.getCarController().setCarTarget(270, 415);
                break;

            case WASHING:
                customer = servicePoints[1].removeQueue();
                customer.getCarController().setCarTarget(270, 500);
                myEngine.doService(customer, EventType.WASHING, 1);
                customer.getCarController().setCarTarget(270, 415);
                break;

            case SHOPPING:
                customer = servicePoints[2].removeQueue();
                customer.getCarController().setCarTarget(600, 280);
                myEngine.doService(customer, EventType.SHOPPING, 2);
                customer.getCarController().setCarTarget(600, 415);
                break;

            case PAYING:
                customer = servicePoints[3].removeQueue();
                customer.getCarController().setCarTarget(900, 415);
                myEngine.doService(customer, EventType.PAYING, -1);
                counterUp(exitCustomer);
                break;

            case DRYING:
                customer = servicePoints[4].removeQueue();
                customer.getCarController().setCarTarget(600, 500);
                myEngine.doService(customer, EventType.DRYING, 2);
                customer.getCarController().setCarTarget(600, 415);
                break;
        }
    }

    public void tryCEventsWithUI() {
        for (ServicePoint servicePoint : myEngine.getServicePoints()) {
            if (!servicePoint.isReserved() && servicePoint.isOnQueue()) {
                servicePoint.beginService();
            }
            for (Router router : myEngine.getRouters()) {
                if (!router.isReserved() && router.isOnQueue()) {
                    router.beginService();
                }
            }
        }
    }

    public void updateUI() {
        Platform.runLater(() -> {
            refuellingCustomer.setText(String.valueOf(servicePoints[0].getQueueSize()));
            washingCustomer.setText(String.valueOf(servicePoints[1].getQueueSize()));
            shoppingCustomer.setText(String.valueOf(servicePoints[2].getQueueSize()));
            payingCustomer.setText(String.valueOf(servicePoints[3].getQueueSize()));
            dryingCustomer.setText(String.valueOf(servicePoints[4].getQueueSize()));
        });
    }
}
