package com.pet.parser.events;



/**
 * Interface of the Component for publishing events.
 */
public interface EventsPublisher {
    void publishEvent(CustomEvent event);
}
