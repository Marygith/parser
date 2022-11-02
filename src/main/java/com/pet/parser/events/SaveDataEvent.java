package com.pet.parser.events;

public class SaveDataEvent {
    private byte [] payload;

    public SaveDataEvent(byte [] payload) {
        this.payload = payload;
    }

    public byte[] getPayload() {
        return payload;
    }
}
