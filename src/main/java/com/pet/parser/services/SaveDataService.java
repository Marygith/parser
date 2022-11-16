package com.pet.parser.services;


import org.springframework.context.event.EventListener;

/**
 * Interface of service that saves received byte [] data to the file .
 */
public interface SaveDataService {

    @EventListener
    void saveData(byte[] data);

}
