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
    static MyEngine m = new MyEngine();

    @FXML
    private Button startSimulationButton;

    @FXML
    private Label arrivedCustomer;

    @FXML
    private Label gasCustomer;

    @FXML
    private Label washingCustomer;

    @FXML
    private Label dryerCustomer;

    @FXML
    private Label repairCustomer;

    @FXML
    private Label storeCustomer;

    @FXML
    private Label exitCustomer;

    @FXML
    private void startSimulationButtonClicked() {

        Thread thread = new Thread(() -> {

            System.out.println("startSimulationButtonClicked() called");

            Trace.setTraceLevel(Trace.Level.INFO);


            m.setSimulationTime(1000);

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
        m.initialize();

        while (m.simulate()) {

            Thread.sleep(10);

            Trace.out(Trace.Level.INFO, "\nA-phase: time is " + m.currentTime());
            m.getClock().setClock(m.currentTime());

            Trace.out(Trace.Level.INFO, "\nB-phase:");
            runBEventsWithUI();

            Trace.out(Trace.Level.INFO, "\nC-phase:");
            tryCEventsWithUI();
        }

        m.results();
    }

    private void runBEventsWithUI() {
        while (m.getEventList().getNextEventTime() == m.getClock().getClock()) {
            runEventWithUI(m.getEventList().remove());
        }
    }

    private void runEventWithUI(Event event) {

        Router[] routers = m.getRouters();
        ServicePoint[] servicePoints = m.getServicePoints();

        Customer customer;

        switch ((EventType) event.getEventType()) {
            case ARR:
                customer = new Customer();
                customer.setEventTypesToVisit(); // Random

                // Todo:
                // If the customer do not want anything, add a gas refill default
                // don't know if this is right, further discussing required.
                if (customer.getEventTypesToVisit().isEmpty()) {
                    customer.getEventTypesToVisit().add(EventType.DEP1);
                }

                System.out.println("!!!!!New customer arrives: Customer" + customer.getId() + ", want service " + customer.getEventTypesToVisit());

                routers[0].addQueue(customer);

                Platform.runLater(() -> arrivedCustomer.setText(String.valueOf(parseInt(arrivedCustomer.getText()) + 1)));

                m.getArrivalProcesses().generateNextEvent();
                break;

            // Router events, the 'splits' should be at here
            case Rot1:
                customer = routers[0].removeQueue();

                System.out.println("!!!Customer " + customer.getId() + " leaving router1 and go to " + customer.getEventTypesToVisit().getFirst());

                switch (customer.getEventTypesToVisit().getFirst()) {
                    case DEP1:
                        servicePoints[0].addQueue(customer);

                        Platform.runLater(() -> gasCustomer.setText(String.valueOf(parseInt(gasCustomer.getText()) + 1)));

                        break;
                    case DEP2, DEP5:
                        servicePoints[1].addQueue(customer);

                        Platform.runLater(() -> washingCustomer.setText(String.valueOf(parseInt(washingCustomer.getText()) + 1)));
                        break;
                    case DEP3:
                        servicePoints[2].addQueue(customer);
                        break;
                    case DEP4:
                        servicePoints[3].addQueue(customer);
                        break;
                }
                break;

            case Rot2:
                customer = routers[1].removeQueue();
                if (customer.getEventTypesToVisit().contains(EventType.DEP5)) {
                    servicePoints[4].addQueue(customer);

                    System.out.println("!!!Customer " + customer.getId() + " leaving router2 and go to DEP5.");

                } else {
                    routers[2].addQueue(customer);

                    System.out.println("!!!Customer " + customer.getId() + " leaving router2 and go to router3.");

                }
                break;

            case Rot3:
                customer = routers[2].removeQueue();
                if (customer.getEventTypesToVisit().isEmpty()) {
                    customer.setRemovalTime(Clock.getInstance().getClock());
                    customer.reportResults();
                } else {
                    routers[0].addQueue(customer);

                    System.out.println("!!!Customer " + customer.getId() + " leaving router3 and reenter the system.");

                }
                break;

            // ServicePoint events
            case DEP1:
                customer = servicePoints[0].removeQueue();
                customer.finishService(EventType.DEP1);

                System.out.println("!!!Customer " + customer.getId() + " leaving DEP1.");

                routers[2].addQueue(customer);

                Platform.runLater(() -> gasCustomer.setText(String.valueOf(parseInt(gasCustomer.getText()) - 1)));

                break;

            case DEP2:
                customer = servicePoints[1].removeQueue();
                customer.finishService(EventType.DEP2);

                System.out.println("!!!Customer " + customer.getId() + " leaving DEP2.");

                routers[1].addQueue(customer);

                Platform.runLater(() -> washingCustomer.setText(String.valueOf(parseInt(washingCustomer.getText()) - 1)));
                break;

            case DEP3:
                customer = servicePoints[2].removeQueue();
                customer.finishService(EventType.DEP3);

                System.out.println("!!!Customer " + customer.getId() + " leaving DEP3.");

                routers[2].addQueue(customer);
                break;

            case DEP4:
                customer = servicePoints[3].removeQueue();
                customer.finishService(EventType.DEP4);

                System.out.println("!!!Customer " + customer.getId() + " leaving DEP4.");

                routers[2].addQueue(customer);
                break;

            case DEP5:
                customer = servicePoints[4].removeQueue();
                customer.finishService(EventType.DEP5);

                System.out.println("!!!Customer " + customer.getId() + " leaving DEP5.");

                routers[2].addQueue(customer);
                break;
        }
    }

    public void tryCEventsWithUI() {
        for (ServicePoint servicePoint : m.getServicePoints()) {
            if (!servicePoint.isReserved() && servicePoint.isOnQueue()) {
                servicePoint.beginService();
            }
            for (Router router : m.getRouters()) {
                if (!router.isReserved() && router.isOnQueue()) {
                    router.beginService();
                }
            }
        }
    }
}
