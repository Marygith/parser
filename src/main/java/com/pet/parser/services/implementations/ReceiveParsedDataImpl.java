package com.pet.parser.services.implementations;

import com.pet.parser.events.CustomEvent;
import com.pet.parser.events.EventsPublisherImpl;
import com.pet.parser.services.ParserService;
import com.pet.parser.services.ReceiveParsedData;
import com.pet.parser.services.SaveDataService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


/**
 * <p>{@link ReceiveParsedData } implementation.</p>
 * Service class that receives parsed data from the  {@link ParserService}
 * and passes it to the {@link SaveDataService}.
 */
@Service
public class ReceiveParsedDataImpl implements ReceiveParsedData {


    private final SaveDataService saveDataServiceImpl;

    public ReceiveParsedDataImpl(SaveDataService saveDataServiceImpl) {
        this.saveDataServiceImpl = saveDataServiceImpl;
    }

    /**
     * <p>A method listening for {@link CustomEvent} event, which contains parsed byte [] data.</p>
     * <p>Data is passed to the {@link SaveDataService}.</p>
     *
     * @param event CustomEvent that is published by {@link EventsPublisherImpl}.
     */
    @Override
    @EventListener(condition = "#event.eventType eq 'SaveDataEvent'")
    public void handleData(CustomEvent event) {
        saveDataServiceImpl.saveData(event.getPayload());
    }
}
