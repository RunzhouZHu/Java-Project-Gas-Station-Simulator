package model;

import framework.IEventType;

// Event types are defined by the requirements of the simulation model
/**
 * Enum representing the different types of events in the simulation.
 * These events include various service points and routing decisions.
 */
public enum EventType implements IEventType {


    // 5 Departure events, the customer exit each service points and cost time
    // 5 corresponding service points which are:
    /**
     * Event type for refuelling service point.
     */
    REFUELLING,
    /**
     * Event type for washing service point.
     */
    WASHING,
    /**
     * Event type for shopping service point.
     */
    SHOPPING,
    /**
     * Event type for paying service point.
     */
    PAYING,
    /**
     * Event type for drying service point.
     */
    DRYING,
    /**
     * Event type for customer arrival.
     */
    ARRIVE, // One arrive event, one customer enter the Choose Service router.

    // 3 Router events,
    // Choose Service Router
    /**
     * Event type for choosing service router.
     */
    Rot1,
    // Dry or not Router
    /**
     * Event type for deciding whether to dry or not.
     */
        Rot2,
    // Pay or Choose another service Router
    /**
     * Event type for deciding whether to pay or choose another service.
     */
        Rot3,
}
