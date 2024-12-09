package controller;

import framework.Event;
import javafx.application.Platform;
import javafx.scene.control.Label;
import model.*;

import static java.lang.Integer.parseInt;


public class MyEngineController {

    public void counterUp(Label counter) {
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

    public void tryCEventsWithUI(MyEngine myEngine) {
        for (ServicePoint servicePoint : myEngine.getServicePoints()) {
            if (!servicePoint.isReserved() && servicePoint.isOnQueue()) {
                servicePoint.beginService();
                switch (servicePoint.getEventType()) {
                    case REFUELLING:

                }
            }
            for (Router router : myEngine.getRouters()) {
                if (!router.isReserved() && router.isOnQueue()) {
                    router.beginService();
                }
            }
        }
    }


    // Main logic method:
    // ---------------------------------------------------------------------------------------
    public void runEventWithUI(Event event, Router[] routers, ServicePoint[] servicePoints, MyEngine myEngine) {

        Customer customer;

        switch ((EventType) event.getEventType()) {
            case ARRIVE:
                customer = new Customer();

                customer.setEventTypesToVisit(); // Random
                System.out.println("!!!!!New customer arrives: Customer" + customer.getId() + ", want service " + customer.getEventTypesToVisit());

                routers[0].addQueue(customer);
                customer.setArriveTime(myEngine.getClock().getClock());

                myEngine.getArrivalProcesses().generateNextEvent();
                break;

            // Router events, the 'splits' should be at here
            case Rot1:
                customer = routers[0].removeQueue();

                System.out.println("!!!Customer " + customer.getId() + " leaving router1 and go to " + customer.getEventTypesToVisit().get(0));

                switch (customer.getEventTypesToVisit().get(0)) {
                    case REFUELLING:
                        servicePoints[0].addQueue(customer);
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
                customer.setExitRefuelTime(myEngine.getClock().getClock());
                myEngine.doService(customer, EventType.REFUELLING, 2);
                break;

            case WASHING:
                customer = servicePoints[1].removeQueue();
                customer.setEnterCashTime(myEngine.getClock().getClock());
                myEngine.doService(customer, EventType.WASHING, 1);
                break;

            case SHOPPING:
                customer = servicePoints[2].removeQueue();
                customer.setExitShopTime(myEngine.getClock().getClock());
                myEngine.doService(customer, EventType.SHOPPING, 2);
                break;

            case PAYING:
                customer = servicePoints[3].removeQueue();
                customer.setEnterCashTime(myEngine.getClock().getClock());
                myEngine.getCustomers().add(customer);
                myEngine.doService(customer, EventType.PAYING, -1);
                break;

            case DRYING:
                customer = servicePoints[4].removeQueue();
                customer.setExitCashTime(myEngine.getClock().getClock());
                myEngine.doService(customer, EventType.DRYING, 2);
                break;
        }
    }
}
