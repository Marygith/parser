package com.pet.parser.services;

import com.pet.parser.events.CustomEvent;
import org.springframework.context.event.EventListener;

/**
 * Interface of service that receives data parsed by {@link ParserService }.
 */
public interface ReceiveParsedData {

    @EventListener
    void handleData(CustomEvent event);
}
