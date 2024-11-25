package model;

import eduni.distributions.Normal;
import framework.ArrivalProcess;
import framework.Engine;

public class MyEngine extends Engine {
    private ArrivalProcess arrivalProcess;
    private ServicePoint[] servicePoints;
    public static final boolean TEXTDEMO = true;
    public static final boolean FIXEDARRIVALTIMES = false;
    public static final boolean FIXEDSERVICETIMES = false;

    /*
    Simulation case: a gas station with 5 different service points
    ┌────────────────────────────────────┐
    │         Choose Service             │◄──┐
    └─┬─────────┬─────────┬───────────┬──┘   │
      │         │         │           │      │
    ┌─▼─┐    ┌──▼──┐   ┌──▼───┐    ┌──▼──┐   │
    │Gas│    │Wash │   │Repair│    │Store│   │
    └─┬─┘    └──┬──┘   └──┬───┘    └──┬──┘   │
      │         │         │           │      │
      │      ┌──▼──┐      │           │      │
      │      │Dryer│      │           │      │
      │      └──┬──┘      │           │      │
      │         │         │           │      │
    ┌─▼─────────▼─────────▼───────────▼───┐  │
    │   Exit or Choose another service    ├──┘
    └──────┬──────────────────────────────┘
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


    }



}
