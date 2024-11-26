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
        routers[0] = new Router(eventList, new HashMap<>(){{
            put(EventType.Rot11, 25);
            put(EventType.Rot12, 25);
            put(EventType.Rot13, 25);
            put(EventType.Rot14, 25);
        }});

        // Router 2, Dryer or not Router
        routers[1] = new Router(eventList, new HashMap<>(){{
            put(EventType.Rot21, 1);
            put(EventType.Rot22, 2);
        }});

        // Router 3, Exit or Choose another service Router
        routers[2] = new Router(eventList, new HashMap<>(){{
            put(EventType.Rot31, 1);
            put(EventType.Rot32, 2);
        }});

        // Set arrival process
        arrivalProcess = new ArrivalProcess(new Negexp(15, 5), eventList, EventType.ARR);
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
                routers[0].addQueue(new Customer());
                arrivalProcess.generateNextEvent();
                break;

            case Rot11:
                customer = routers[0].removeQueue();
                servicePoints[0].addQueue(customer);
                break;

            case Rot12:
                customer = routers[0].removeQueue();
                servicePoints[1].addQueue(customer);
                break;

            case Rot13:
                customer = routers[0].removeQueue();
                servicePoints[2].addQueue(customer);
                break;

            case Rot14:
                customer = routers[0].removeQueue();
                servicePoints[3].addQueue(customer);
                break;


            case Rot21:
                customer = routers[1].removeQueue();
                routers[2].addQueue(customer);
                break;

            case Rot22:
                customer = routers[1].removeQueue();
                servicePoints[4].addQueue(customer);
                break;

            case Rot31:
                customer = routers[2].removeQueue();
                customer.setRemovalTime(Clock.getInstance().getClock());
                customer.reportResults();
                break;

            case Rot32:
                customer = routers[2].removeQueue();
                routers[1].addQueue(customer);
                break;

            case DEP1:
                customer = servicePoints[0].removeQueue();
                routers[3].addQueue(customer);
                break;

            case DEP2:
                customer = servicePoints[1].removeQueue();
                routers[2].addQueue(customer);
                break;

            case DEP3:
                customer = servicePoints[2].removeQueue();
                routers[3].addQueue(customer);
                break;

            case DEP4:
                customer = servicePoints[3].removeQueue();
                routers[3].addQueue(customer);
                break;

            case DEP5:
                customer = servicePoints[4].removeQueue();
                routers[3].addQueue(customer);
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
    }

    @Override
    protected void results() {
        System.out.println("Simulation ended at " + Clock.getInstance().getClock());
        System.out.println("123123123");
    }
}
