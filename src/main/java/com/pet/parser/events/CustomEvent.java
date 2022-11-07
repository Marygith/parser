package com.pet.parser.events;

public class CustomEvent {


    private final byte [] payload;

    private final String eventType;

    public CustomEvent(byte[] payload, String eventType)
    {
        this.payload = payload;
        this.eventType = eventType;
    }


    public byte[] getPayload() {
        return payload;
    }

    public String getEventType() {
        return eventType;
    }
}
