package com.pet.parser.TCP;

import com.pet.parser.Service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHeaders;


@MessageEndpoint
public class EndPoint {

    DataService dataService;

    @Autowired
    public EndPoint(DataService  dataService) {
        this. dataService=  dataService;
    }

    @ServiceActivator(inputChannel = "inboundChannel")
    public byte[] processMessage(byte[] payload, MessageHeaders messageHeaders) {
        return dataService.processMessage(payload, messageHeaders);
    }
}
