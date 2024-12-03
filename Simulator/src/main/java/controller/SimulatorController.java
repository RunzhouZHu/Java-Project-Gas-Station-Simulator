package controller;

import framework.Clock;
import framework.Event;
import framework.Trace;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.*;

import static java.lang.Integer.parseInt;

public class SimulatorController {
    static MyEngine myEngine = new MyEngine();

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

    // Run simulation with UI
    private void runSimulation() throws InterruptedException {
        myEngine.initialize();

        while (myEngine.simulate()) {

            Thread.sleep(10);

            Trace.out(Trace.Level.INFO, "\nA-phase: time is " + myEngine.currentTime());
            myEngine.getClock().setClock(myEngine.currentTime());

            Trace.out(Trace.Level.INFO, "\nB-phase:");
            runBEventsWithUI();

            Trace.out(Trace.Level.INFO, "\nC-phase:");
            tryCEventsWithUI();
        }

        myEngine.results();
    }

    private void runBEventsWithUI() {
        while (myEngine.getEventList().getNextEventTime() == myEngine.getClock().getClock()) {
            runEventWithUI(myEngine.getEventList().remove());
        }
    }

    private void runEventWithUI(Event event) {

        Router[] routers = myEngine.getRouters();
        ServicePoint[] servicePoints = myEngine.getServicePoints();

        Customer customer;

        switch ((EventType) event.getEventType()) {
            case ARRIVE:
                customer = new Customer();
                customer.setEventTypesToVisit(); // Random
                System.out.println("!!!!!New customer arrives: Customer" + customer.getId() + ", want service " + customer.getEventTypesToVisit());

                routers[0].addQueue(customer);

                Platform.runLater(() -> arrivedCustomer.setText(String.valueOf(parseInt(arrivedCustomer.getText()) + 1)));

                myEngine.getArrivalProcesses().generateNextEvent();
                break;

            // Router events, the 'splits' should be at here
            case Rot1:
                customer = routers[0].removeQueue();

                System.out.println("!!!Customer " + customer.getId() + " leaving router1 and go to " + customer.getEventTypesToVisit().get(0));

                switch (customer.getEventTypesToVisit().get(0)) {
                    case REFUELLING:
                        servicePoints[0].addQueue(customer);

                        Platform.runLater(() -> refuellingCustomer.setText(String.valueOf(parseInt(refuellingCustomer.getText()) + 1)));

                        break;
                    case WASHING, DRYING:
                        servicePoints[1].addQueue(customer);

                        Platform.runLater(() -> washingCustomer.setText(String.valueOf(parseInt(washingCustomer.getText()) + 1)));
                        break;
                    case SHOPPING:
                        servicePoints[2].addQueue(customer);

                        Platform.runLater(() -> shoppingCustomer.setText(String.valueOf(parseInt(shoppingCustomer.getText()) + 1)));
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
                    Platform.runLater(() -> dryingCustomer.setText(String.valueOf(parseInt(dryingCustomer.getText()) + 1)));

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

                    Platform.runLater(() -> payingCustomer.setText(String.valueOf(parseInt(payingCustomer.getText()) + 1)));
                } else {
                    routers[0].addQueue(customer);

                    System.out.println("!!!Customer " + customer.getId() + " leaving router3 and reenter the system.");

                }
                break;

            // ServicePoint events
            case REFUELLING:
                customer = servicePoints[0].removeQueue();
                customer.finishService(EventType.REFUELLING);

                System.out.println("!!!Customer " + customer.getId() + " leaving REFUELLING.");

                routers[2].addQueue(customer);

                Platform.runLater(() -> refuellingCustomer.setText(String.valueOf(parseInt(refuellingCustomer.getText()) - 1)));

                break;

            case WASHING:
                customer = servicePoints[1].removeQueue();
                customer.finishService(EventType.WASHING);

                System.out.println("!!!Customer " + customer.getId() + " leaving WASHING.");

                routers[1].addQueue(customer);

                Platform.runLater(() -> washingCustomer.setText(String.valueOf(parseInt(washingCustomer.getText()) - 1)));
                break;

            case SHOPPING:
                customer = servicePoints[2].removeQueue();
                customer.finishService(EventType.SHOPPING);

                System.out.println("!!!Customer " + customer.getId() + " leaving SHOPPING.");

                routers[2].addQueue(customer);

                Platform.runLater(() -> shoppingCustomer.setText(String.valueOf(parseInt(shoppingCustomer.getText()) - 1)));
                break;

            case PAYING:
                customer = servicePoints[3].removeQueue();
                customer.finishService(EventType.PAYING);

                System.out.println("!!!Customer " + customer.getId() + " leaving PAYING.");

                customer.reportResults();

                Platform.runLater(() -> payingCustomer.setText(String.valueOf(parseInt(payingCustomer.getText()) - 1)));
                Platform.runLater(() -> exitCustomer.setText(String.valueOf(parseInt(exitCustomer.getText()) + 1)));

                break;

            case DRYING:
                customer = servicePoints[4].removeQueue();
                customer.finishService(EventType.DRYING);

                System.out.println("!!!Customer " + customer.getId() + " leaving DRYING.");

                routers[2].addQueue(customer);

                Platform.runLater(() -> dryingCustomer.setText(String.valueOf(parseInt(dryingCustomer.getText()) - 1)));
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
}
