package com.pet.parser.services;

import com.pet.parser.events.TcpEvent;
import org.springframework.context.event.EventListener;

public interface SendByTcp {

    @EventListener
    void sendData(TcpEvent tcpEvent);
}
