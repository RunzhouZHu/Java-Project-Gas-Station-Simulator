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
                    ┌─────────────────▼──────────────────┐
                    │         Choose Service             │◄──┐
                    └─┬─────────┬─────────┬───────────┬──┘   │
                      │         │         │           │      │
                    ┌─▼─┐    ┌──▼──┐   ┌──▼───┐    ┌──▼──┐   │
                    │Gas│    │Wash │   │Repair│    │Store│   │
                    └─┬─┘    └──┬──┘   └──┬───┘    └──┬──┘   │
                      │         │         │           │      │
                      │      ┌──▼──┐      │           │      │
                      │      │Dryer│      │           │      │
                      │   ┌──┤or   │      │           │      │
                      │   │  │not  │      │           │      │
                      │   │  └──┬──┘      │           │      │
                      │   │     │         │           │      │
                      │   │  ┌──▼──┐      │           │      │
                      │   │  │Dryer│      │           │      │
                      │   │  └──┬──┘      │           │      │
                      │   │     │         │           │      │
                    ┌─▼───▼─────▼─────────▼───────────▼───┐  │
                    │   Exit or Choose another service    ├──┘
                    └─────────────────┬───────────────────┘
                                      │
                                      ▼
                                     EXIT
     */

    public MyEngine() {
        servicePoints = new ServicePoint[5];

        // Set the service points, remember to set suit mean and variance for service time!
        // Gas
        servicePoints[0] = new ServicePoint(new Normal(10,6), eventList, EventType.DEP1);
        // Wash
        servicePoints[1] = new ServicePoint(new Normal(10,6), eventList, EventType.DEP2);
        // Repair
        servicePoints[2] = new ServicePoint(new Normal(10,6), eventList, EventType.DEP3);
        // Store
        servicePoints[3] = new ServicePoint(new Normal(10,6), eventList, EventType.DEP4);
        // Dryer
        servicePoints[4] = new ServicePoint(new Normal(10,6), eventList, EventType.DEP5);

        // Set 3 Routers
        routers = new Router[3];
        // Router 1, Choose Service Router
        routers[0] = new Router(eventList, EventType.Rot1);

        // Router 2, Dryer or not Router
        routers[1] = new Router(eventList, EventType.Rot2);

        // Router 3, Exit or Choose another service Router
        routers[2] = new Router(eventList, EventType.Rot3);

        // Set arrival process
        arrivalProcess = new ArrivalProcess(new Normal(15, 5), eventList, EventType.ARR);
    }

    @Override
    protected void initialize() {
        arrivalProcess.generateNextEvent();
    }

    @Override
    protected void runEvent(Event event) {
        Customer customer;

        switch ((EventType)event.getEventType()) {
            case ARR:
                customer = new Customer();
                customer.setEventTypesToVisit(); // Random

                // Todo:
                // If the customer do not want anything, add a gas refill default
                // don't know if this is right, further discussing required.
                if (customer.getEventTypesToVisit().isEmpty()){
                    customer.getEventTypesToVisit().add(EventType.DEP1);
                }

                System.out.println("!!!!!New customer arrives: Customer" + customer.getId() + ", want service " + customer.getEventTypesToVisit());

                routers[0].addQueue(customer);
                arrivalProcess.generateNextEvent();
                break;

            // Router events, the 'splits' should be at here
            case Rot1:
                customer = routers[0].removeQueue();

                System.out.println("!!!Customer " + customer.getId() + " leaving router1 and go to " + customer.getEventTypesToVisit().get(0));

                switch (customer.getEventTypesToVisit().get(0)) {
                    case DEP1:
                        servicePoints[0].addQueue(customer);
                        break;
                    case DEP2, DEP5:
                        servicePoints[1].addQueue(customer);
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
                break;

            case DEP2:
                customer = servicePoints[1].removeQueue();
                customer.finishService(EventType.DEP2);

                System.out.println("!!!Customer " + customer.getId() + " leaving DEP2.");

                routers[1].addQueue(customer);
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
    protected void results() {
        System.out.println("Simulation ended at " + Clock.getInstance().getClock());
        System.out.println("Result, haven't done this part yet.");
    }
}
