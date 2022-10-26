package com.pet.parser.tcp;

import com.pet.parser.services.GeneralService;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;


@MessageEndpoint
public class EndPoint {


    private final GeneralService generalService;

    public EndPoint(GeneralService generalService) {
        this.generalService = generalService;
    }

    @ServiceActivator(inputChannel = "inboundChannel", outputChannel = "outboundChannel")
    public void processMessage(byte[] payload) {

        generalService.processMessage(payload);

    }
}
