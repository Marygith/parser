package com.pet.parser.events;

/**
 * An event class, it has two fields - <i>byte [] payload</i> and <i>String eventType</i>.
 */
public class CustomEvent {


    private final byte[] payload;

    private final String eventType;

    public CustomEvent(byte[] payload, String eventType) {
        this.payload = payload;
        this.eventType = eventType;
    }

    /**
     * A standard getter for <i>payload</i> field.
     *
     * @return byte [] payload
     */

    public byte[] getPayload() {
        return payload;
    }

    /**
     * A standard getter for <i>eventType</i> field.
     *
     * @return String eventType
     */
    public String getEventType() {
        return eventType;
    }
}
