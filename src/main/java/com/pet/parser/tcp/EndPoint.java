package com.pet.parser.tcp;

import com.pet.parser.services.GeneralService;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.nio.ByteBuffer;


@MessageEndpoint
public class EndPoint {


    private final GeneralService generalService;

    public EndPoint(GeneralService generalService) {
        this.generalService = generalService;
    }

    @ServiceActivator(inputChannel = "inboundChannel", outputChannel = "outboundChannel")
    public void processMessage(byte[] payload) {

        byte[] del = new byte[]{13, 10};
        int len = payload.length + 2;

        ByteBuffer buffer = ByteBuffer.allocate(len);
        buffer.put(payload);
        buffer.put(del);
        buffer.clear();

        generalService.processMessage(buffer);

    }
}
