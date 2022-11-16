package com.pet.parser.services;

/**
 * Interface of service that parses received byte [] data.
 */
public interface ParserService {

    void parsePayload(byte[] payload);

}
