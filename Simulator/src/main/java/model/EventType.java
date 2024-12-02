package model;

import framework.IEventType;

// Event types are defined by the requirements of the simulation model
public enum EventType implements IEventType {

    ARRIVE, // One arrive event, one customer enter the Choose Service router.

    // 5 Departure events, the customer exit each service points and cost time
    // 5 corresponding service points which are:
    REFUELLING, WASHING, SHOPPING, PAYING, DRYING,

    // 3 Router events,
    // Choose Service Router
        Rot1,
    // Dry or not Router
        Rot2,
    // Pay or Choose another service Router
        Rot3,
}
