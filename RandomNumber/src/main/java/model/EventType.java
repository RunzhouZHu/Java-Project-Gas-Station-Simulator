package model;

import framework.IEventType;

// TODO:
// Event types are defined by the requirements of the simulation model
public enum EventType implements IEventType {
    // There are total 5 different service points
    // gas, wash, dryer, repair, store
    // each have own ARRs and DEPs

    //  gas   wash  dryer repair store
        ARR1, ARR2, ARR3, ARR4,  ARR5,
        DEP1, DEP2, DEP3, DEP4,  DEP5;
}
