package com.pet.parser.services;


import com.pet.parser.events.SaveDataEvent;
import org.springframework.context.event.EventListener;

public interface SaveDataService {

    @EventListener
    void saveData(SaveDataEvent saveDataEvent);

}
