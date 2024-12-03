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

                        counterUp(refuellingCustomer);

                        break;
                    case WASHING, DRYING:
                        servicePoints[1].addQueue(customer);

                        counterUp(washingCustomer);
                        break;
                    case SHOPPING:
                        servicePoints[2].addQueue(customer);

                        counterUp(shoppingCustomer);
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

                    counterUp(dryingCustomer);

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

                    counterUp(payingCustomer);
                } else {
                    routers[0].addQueue(customer);

                    System.out.println("!!!Customer " + customer.getId() + " leaving router3 and reenter the system.");

                }
                break;

            // ServicePoint events
            case REFUELLING:
                myEngine.doService(EventType.REFUELLING, 2);
                counterDown(refuellingCustomer);
                break;

            case WASHING:
                myEngine.doService(EventType.WASHING, 1);
                counterDown(washingCustomer);
                break;

            case SHOPPING:
                myEngine.doService(EventType.SHOPPING, 2);
                counterDown(shoppingCustomer);
                break;

            case PAYING:
                myEngine.doService(EventType.PAYING, -1);
                counterDown(payingCustomer);
                counterUp(exitCustomer);
                break;

            case DRYING:
                myEngine.doService(EventType.DRYING, 2);
                counterDown(dryingCustomer);
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
