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

    // 3 Choose Service Router events, the customer exit the Choose Service router,
    // and has 4 options:
    // Gas,   Wash,  Repair, Store
       Rot11, Rot12, Rot13,  Rot14,

    // 2 Dryer or not Router events, customer exit the wash service point and
    // choose whether to go to the dryer
    // 2 options:
       Rot21,// Exit or Choose another service Router
       Rot22,// Dryer

    // 2 Exit or choose another service events
       Rot31,// Exit
       Rot32,// Choose another service
}
