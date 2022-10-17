package com.pet.parser.tcp;

import com.pet.parser.services.GeneralService;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHeaders;


@MessageEndpoint
public class EndPoint {

    private final GeneralService generalService;

    public EndPoint(GeneralService generalService) {
        this.generalService = generalService;
    }

    @ServiceActivator(inputChannel = "inboundChannel", outputChannel = "outboundChannel")
    public byte[] processMessage(byte[] payload, MessageHeaders messageHeaders) {
        return generalService.processMessage(payload, messageHeaders);
    }
}
