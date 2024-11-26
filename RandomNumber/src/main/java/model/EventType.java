package model;

import framework.IEventType;

// Event types are defined by the requirements of the simulation model
public enum EventType implements IEventType {
    /*
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

    ARR, // One arrive event, one customer enter the Choose Service router.

    // 5 Departure events, the customer exit each service points and cost time
    // 5 corresponding service points which are:
    // Gas,  Wash, Repair, Store, Dryer
       DEP1, DEP2, DEP3,   DEP4,  DEP5,

    // 3 Router events,
    // Choose Service Router
        Rot1,
    // Dryer or not Router
        Rot2,
    // Exit or Choose another service Router
        Rot3,
}
