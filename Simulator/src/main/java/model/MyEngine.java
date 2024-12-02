package model;

import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import framework.ArrivalProcess;
import framework.Clock;
import framework.Engine;
import framework.Event;

import java.util.HashMap;

public class MyEngine extends Engine {
    private ArrivalProcess arrivalProcess;
    private ServicePoint[] servicePoints;
    private Router[] routers;
    public static final boolean TEXTDEMO = true;
    public static final boolean FIXEDARRIVALTIMES = false;
    public static final boolean FIXEDSERVICETIMES = false;

    /*
    Simulation case: a gas station with 5 different service points

                               ARRIVE
                                 │
                ┌----------------▼-------------┐
                │        Choose Service        │◄--┐
                └-----┬----------┬---------┬---┘   │
                      │          │         │       │
                ┌-----▼----┐ ┌---▼---┐ ┌---▼----┐  │
                │Refuelling│ │Washing│ │Shopping│  │
                └-----┬----┘ └---┬---┘ └---┬----┘  │
                      │          │         │       │
                      │      ┌---▼---┐     │       │
                      │      │Drying │     │       │
                      │   ┌--┤  or   │     │       │
                      │   │  │  not  │     │       │
                      │   │  └---┬---┘     │       │
                      │   │      │         │       │
                      │   │  ┌---▼---┐     │       │
                      │   │  │Drying │     │       │
                      │   │  └---┬---┘     │       │
                      │   │      │         │       │
                ┌-----▼---▼------▼---------▼---┐   │
                │Pay or Choose another service ├---┘
                └----------------┬-------------┘
                                 │
                             ┌---▼---┐
                             │Paying │
                             └---┬---┘
                                 │
                                 ▼
                                EXIT
     */

    public MyEngine() {
        servicePoints = new ServicePoint[5];

        // Set the service points, remember to set suit mean and variance for service time!
        // Refuelling
        servicePoints[0] = new ServicePoint(new Normal(10,6), eventList, EventType.REFUELLING);
        // Washing
        servicePoints[1] = new ServicePoint(new Normal(10,6), eventList, EventType.WASHING);
        // Shopping
        servicePoints[2] = new ServicePoint(new Normal(10,6), eventList, EventType.SHOPPING);
        // Paying
        servicePoints[3] = new ServicePoint(new Normal(10,6), eventList, EventType.PAYING);
        // Drying
        servicePoints[4] = new ServicePoint(new Normal(10,6), eventList, EventType.DRYING);

        // Set 3 Routers
        routers = new Router[3];
        // Router 1, Choose Service Router
        routers[0] = new Router(eventList, EventType.Rot1);

        // Router 2, Dry or not Router
        routers[1] = new Router(eventList, EventType.Rot2);

        // Router 3, Pay or Choose another service Router
        routers[2] = new Router(eventList, EventType.Rot3);

        // Set arrival process
        arrivalProcess = new ArrivalProcess(
            new Normal(15, 5), eventList, EventType.ARRIVE
        );
    }

    @Override
    public void initialize() {
        arrivalProcess.generateNextEvent();
    }

    @Override
    protected void runEvent(Event event) {
        Customer customer;

        switch ((EventType)event.getEventType()) {
            case ARRIVE:
                customer = new Customer();
                customer.setEventTypesToVisit(); // Random
                System.out.println("!!!!!New customer arrives: Customer" + customer.getId() + ", want service " + customer.getEventTypesToVisit());

                routers[0].addQueue(customer);
                arrivalProcess.generateNextEvent();
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
                customer.finishService(EventType.REFUELLING);

                System.out.println("!!!Customer " + customer.getId() + " leaving REFUELLING.");

                routers[2].addQueue(customer);
                break;

            case WASHING:
                customer = servicePoints[1].removeQueue();
                customer.finishService(EventType.WASHING);

                System.out.println("!!!Customer " + customer.getId() + " leaving WASHING.");

                routers[1].addQueue(customer);
                break;

            case SHOPPING:
                customer = servicePoints[2].removeQueue();
                customer.finishService(EventType.SHOPPING);

                System.out.println("!!!Customer " + customer.getId() + " leaving SHOPPING.");

                routers[2].addQueue(customer);
                break;

            case PAYING:
                customer = servicePoints[3].removeQueue();
                customer.finishService(EventType.PAYING);

                System.out.println("!!!Customer " + customer.getId() + " leaving PAYING.");

                customer.reportResults();
                break;

            case DRYING:
                customer = servicePoints[4].removeQueue();
                customer.finishService(EventType.DRYING);

                System.out.println("!!!Customer " + customer.getId() + " leaving DRYING.");

                routers[2].addQueue(customer);
                break;
        }
    }

    @Override
    protected void tryCEvents() {
        for (ServicePoint servicePoint : servicePoints) {
            if (!servicePoint.isReserved() && servicePoint.isOnQueue()) {
                servicePoint.beginService();
            }
        }
        for (Router router : routers) {
            if (!router.isReserved() && router.isOnQueue()) {
                router.beginService();
            }
        }
    }

    @Override
    public void results() {
        System.out.println("Simulation ended at " + Clock.getInstance().getClock());
        System.out.println("Result, haven't done this part yet.");
    }

    public Router[] getRouters() {
        return routers;
    }

    public ServicePoint[] getServicePoints() {
        return servicePoints;
    }

    public ArrivalProcess getArrivalProcesses() {
        return arrivalProcess;
    }

}
