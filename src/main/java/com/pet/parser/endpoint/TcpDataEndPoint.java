package com.pet.parser.endpoint;

import com.pet.parser.events.CustomEvent;
import com.pet.parser.services.GeneralService;
import com.pet.parser.tcp.TcpHandler;
import org.springframework.context.event.EventListener;
import org.springframework.integration.annotation.MessageEndpoint;


/**
 * A class that serves as an endpoint of data reception from server and
 * passes data to services.
 *
 * @see TcpHandler
 * @see GeneralService
 */
@MessageEndpoint
public class TcpDataEndPoint {


    private final GeneralService generalService;

    public TcpDataEndPoint(GeneralService generalService) {
        this.generalService = generalService;
    }

    /**
     * A method listening for CustomEvent event, which contains byte [] data received from server.
     * Received data is passed to GeneralService, which processes this data.
     *
     * @see CustomEvent
     */
    @EventListener(condition = "#event.eventType eq 'GetDataEvent'")
    public void processMessage(CustomEvent event) {

        byte[] payload = event.getPayload();


        generalService.processMessage(payload);

    }
}
