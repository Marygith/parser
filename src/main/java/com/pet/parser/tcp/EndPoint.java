package com.pet.parser.tcp;

import com.pet.parser.services.interfaces.DataService;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHeaders;


@MessageEndpoint
public class EndPoint {

    private final DataService dataService;

    public EndPoint(DataService  dataService) {
        this.dataService=  dataService;
    }

    @ServiceActivator(inputChannel = "inboundChannel")
    public byte[] processMessage(byte[] payload, MessageHeaders messageHeaders) {
        return dataService.processMessage(payload, messageHeaders);
    }
}
