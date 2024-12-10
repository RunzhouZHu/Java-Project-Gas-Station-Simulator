package model;

import eduni.distributions.Normal;
import framework.ArrivalProcess;
import framework.Clock;
import framework.Engine;
import framework.Event;

import java.util.ArrayList;
/**
 * The MyEngine class extends the Engine class and simulates a gas station with multiple service points.
 * It handles the arrival of customers, their routing through various service points, and the collection of simulation results.
 */
public class MyEngine extends Engine {

    private ArrayList<Customer> customerResults = new ArrayList<>();

    private ArrivalProcess arrivalProcess;
    private ServicePoint[] servicePoints;
    private Router[] routers;
    public static final boolean TEXTDEMO = true;
    public static final boolean FIXEDARRIVALTIMES = false;
    public static final boolean FIXEDSERVICETIMES = false;

    // Set parameters
    // ------------------------------------------------------------
    private Double refuelM;
    private Double refuelV;
    private Double washM;
    private Double washV;
    private Double shopM;
    private Double shopV;
    private Double payM;
    private Double payV;
    private Double dryM;
    private Double dryV;

    private Double arrM;
    private Double arrV;

    // ------------------------------------------------------------

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
    /**
     * Constructs a MyEngine instance with specified parameters for service points and arrival process.
     *
     * @param refuelM mean service time for refuelling
     * @param refuelV variance of service time for refuelling
     * @param washM mean service time for washing
     * @param washV variance of service time for washing
     * @param shopM mean service time for shopping
     * @param shopV variance of service time for shopping
     * @param payM mean service time for paying
     * @param payV variance of service time for paying
     * @param dryM mean service time for drying
     * @param dryV variance of service time for drying
     * @param arrM mean arrival time
     * @param arrV variance of arrival time
     */
    public MyEngine(Double refuelM, Double refuelV,Double washM, Double washV, Double shopM, Double shopV, Double payM, Double payV, Double dryM, Double dryV, Double arrM, Double arrV) {
        servicePoints = new ServicePoint[5];

        // Set the service points, remember to set suit mean and variance for service time!
        // Refuelling
        servicePoints[0] = new ServicePoint(new Normal(refuelM,refuelV), eventList, EventType.REFUELLING);
        // Washing
        servicePoints[1] = new ServicePoint(new Normal(washM,washV), eventList, EventType.WASHING);
        // Shopping
        servicePoints[2] = new ServicePoint(new Normal(shopM,shopV), eventList, EventType.SHOPPING);
        // Paying
        servicePoints[3] = new ServicePoint(new Normal(payM,payV), eventList, EventType.PAYING);
        // Drying
        servicePoints[4] = new ServicePoint(new Normal(dryM,dryV), eventList, EventType.DRYING);

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
            new Normal(arrM, arrV), eventList, EventType.ARRIVE
        );
    }
    /**
     * Handles the service completion for a customer at a specific service point and routes them to the next router.
     *
     * @param customer the customer being serviced
     * @param eventType the type of event/service point
     * @param routerIndex the index of the next router to route the customer to
     */
    public void doService(Customer customer, EventType eventType, int routerIndex) {
        try {
            customer.finishService(eventType);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(
            "!!!Customer " + customer.getId() + " leaving " + eventType + "."
        );
        if (routerIndex == -1) {
            customer.reportResults();
        } else {
            routers[routerIndex].addQueue(customer);
        }

    }

    /**
     * Handles the service completion for a customer at a specific service point and routes them to the next router.
     *
     * @param eventType the type of event/service point
     * @param routerIndex the index of the next router to route the customer to
     */
    public void doService(EventType eventType, int routerIndex) {
        int index = eventType.ordinal();
        Customer customer = servicePoints[index].removeQueue();
        try {
            customer.finishService(eventType);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(
            "!!!Customer " + customer.getId() + " leaving " + eventType + "."
        );
        if (routerIndex == -1) {
            customer.reportResults();
        } else {
            routers[routerIndex].addQueue(customer);
        }

    }

    /**
     * Initializes the simulation by generating the first arrival event.
     */
    @Override
    public void initialize() {
        Clock clock = Clock.getInstance();

        arrivalProcess.generateNextEvent();

    }

    /**
     * Runs the specified event, handling customer arrivals, routing, and service completions.
     *
     * @param event the event to run
     */
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
                doService(EventType.REFUELLING, 2);
                break;

            case WASHING:
                doService(EventType.WASHING, 1);
                break;

            case SHOPPING:
                doService(EventType.SHOPPING, 2);
                break;

            case PAYING:
                doService(EventType.PAYING, -1);
                break;

            case DRYING:
                doService(EventType.DRYING, 2);
                break;
        }
    }

    /**
     * Checks and begins service for customers in the queues of service points and routers.
     */
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

    /**
     * Outputs the results of the simulation, including various performance metrics.
     */
    @Override
    public void results() {
        System.out.println("Simulation ended at " + Clock.getInstance().getClock());

        // Directly observable variables are:
        int arrivalCount = routers[0].getNumberOfArrivedCustomer();
        int completedCount = servicePoints[3].getNumberOfServedCustomer();
        double busyTime = Result.busyTime(customerResults);
        double time = Clock.getInstance().getClock();

        System.out.println("A, arrived clients count (arrival count): " + arrivalCount);
        System.out.println("C, clients serviced count (completed count): " + completedCount);
        System.out.println("B, active time in service point (busy time): " + busyTime);
        System.out.println("T, total simulation time (time): " + time);

        // Derived variables (from the previous variables) are:
        double servicePointUtilization = busyTime / time;
        double serviceThroughput = completedCount / time;
        double serviceTime = busyTime / completedCount;

        System.out.println("U, service point utilization related to the max capacity, U = B/T: " + servicePointUtilization);
        System.out.println("X, service throughput, number of clients serviced related to the time, X = C/T: " + serviceThroughput);
        System.out.println("S, service time, average service time in the service point, S = B/C: " + serviceTime);

        // Additional directly observable variables are:
        double waitingTime = Result.waitTime(customerResults);

        System.out.println("W, waiting time, cumulative response times sum of all clients: " + waitingTime);

        // From these last two, we can further derive the following quantities:
        double responseTime = waitingTime / completedCount;
        double averageQueueLength = waitingTime / time;

        System.out.println("R, response time, average throughput time at the service point, R = W/C: " + responseTime);
        System.out.println("N, average queue length at the service point (including the served) N = W/T: " + averageQueueLength);

        Result.generateResultFile(customerResults, arrivalCount);
    }


    // Getter and setters
    // ----------------------------------------------------------------------
    public ArrayList<Customer> getCustomers() {
        return customerResults;
    }

    public void setCustomers(ArrayList<Customer> customerResults) {
        this.customerResults = customerResults;
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

    public Double getRefuelM() {
        return refuelM;
    }
    public void setRefuelM(Double refuelM) {
        this.refuelM = refuelM;
    }
    public Double getRefuelV() {
        return refuelV;
    }
    public void setRefuelV(Double refuelV) {
        this.refuelV = refuelV;
    }
    public Double getWashM() {
        return washM;
    }
    public void setWashM(Double washM) {
        this.washM = washM;
    }
    public Double getWashV() {
        return washV;
    }
    public void setWashV(Double washV) {
        this.washV = washV;
    }
    public Double getShopM() {
        return shopM;
    }
    public void setShopM(Double shopM) {
        this.shopM = shopM;
    }
    public Double getShopV() {
        return shopV;
    }
    public void setShopV(Double shopV) {
        this.shopV = shopV;
    }
    public Double getPayM() {
        return payM;
    }
    public void setPayM(Double payM) {
        this.payM = payM;
    }
    public Double getPayV() {
        return payV;
    }
    public void setPayV(Double payV) {
        this.payV = payV;
    }
    public Double getDryM() {
        return dryM;
    }
    public void setDryM(Double dryM) {
        this.dryM = dryM;
    }
    public Double getDryV() {
        return dryV;
    }
    public void setDryV(Double dryV) {
        this.dryV = dryV;
    }
    public Double getArrM() {
        return arrM;
    }
    public void setArrM(Double arrM) {
        this.arrM = arrM;
    }
    public Double getArrV() {
        return arrV;
    }
    public void setArrV(Double arrV) {
        this.arrV = arrV;
    }
}
