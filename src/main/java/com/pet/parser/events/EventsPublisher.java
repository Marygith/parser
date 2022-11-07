package com.pet.parser.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventsPublisher {

    private final ApplicationEventPublisher eventPublisher;

    EventsPublisher(ApplicationEventPublisher publisher) {
        this.eventPublisher = publisher;
    }

    public void publishEvent(CustomEvent event) {
        eventPublisher.publishEvent(event);
    }

}