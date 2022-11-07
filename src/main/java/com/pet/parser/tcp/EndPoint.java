package com.pet.parser.tcp;

import com.pet.parser.events.CustomEvent;
import com.pet.parser.services.GeneralService;
import org.springframework.context.event.EventListener;
import org.springframework.integration.annotation.MessageEndpoint;

import java.nio.ByteBuffer;


@MessageEndpoint
public class EndPoint {


    private final GeneralService generalService;

    public EndPoint(GeneralService generalService) {
        this.generalService = generalService;
    }

    @EventListener(condition = "#event.eventType eq 'GetDataEvent'")
    public void processMessage(CustomEvent event) {

        byte[] payload = event.getPayload();
        int len = payload.length;

        ByteBuffer buffer = ByteBuffer.allocate(len);
        buffer.put(payload);
        buffer.clear();

        generalService.processMessage(buffer);

    }
}
