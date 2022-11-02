package com.pet.parser.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventsPublisher {

    private final ApplicationEventPublisher eventPublisher;

    EventsPublisher(ApplicationEventPublisher publisher) {
        this.eventPublisher = publisher;
    }

    public void publishTcpEvent(TcpEvent event) {
        eventPublisher.publishEvent(event);
    }

    public void publishSaveDataEvent(SaveDataEvent event) {
        eventPublisher.publishEvent(event);
    }

 /*   public void publishMsgEvent(String msg) {
        eventPublisher.publishEvent(msg);
    }*/
}