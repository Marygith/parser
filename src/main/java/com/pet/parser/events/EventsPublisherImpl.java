package com.pet.parser.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


/**
 * {@link EventsPublisher} implementation.
 * A class that uses {@link ApplicationEventPublisher}
 * for publishing {@link CustomEvent}events.
 */
@Component
public class EventsPublisherImpl implements EventsPublisher {

    private final ApplicationEventPublisher eventPublisher;

    EventsPublisherImpl(ApplicationEventPublisher publisher) {
        this.eventPublisher = publisher;
    }

    /**
     * A method that publishes <i>event</i>.
     *
     * @param event see {@link CustomEvent}
     */
    @Override
    public void publishEvent(CustomEvent event) {
        eventPublisher.publishEvent(event);
    }

}