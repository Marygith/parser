package com.pet.parser.services;


import com.pet.parser.events.CustomEvent;
import org.springframework.context.event.EventListener;

public interface SaveDataService {

    @EventListener
    void saveData(CustomEvent event);

}
