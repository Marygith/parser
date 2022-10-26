package com.pet.parser.services;

public interface ParserService {

    void parsePayload(byte[] payload);

    byte[] getInfoToBeSaved();

    short getMessageId();

}
