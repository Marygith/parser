package com.pet.parser.services;


/**
 * Interface of service that processes data, received from tcp server.
 */
public interface GeneralService {

    void processMessage(byte[] payload);

}
