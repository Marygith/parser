package com.pet.parser.events;

public class TcpEvent {

    private byte [] payload;

    public TcpEvent(byte [] payload) {
        this.payload = payload;
    }

    public byte[] getPayload() {
        return payload;
    }
}
