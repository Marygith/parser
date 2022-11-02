package com.pet.parser.services.implementations;

import com.pet.parser.events.TcpEvent;
import com.pet.parser.services.SendByTcp;
import com.pet.parser.tcp.ServerConf;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class SendByTcpImpl implements SendByTcp {

    private final ServerConf serverConf;
    private final ServerConf.Gateway gateway;

    public SendByTcpImpl(ServerConf serverConf, ServerConf.Gateway gateway) {
        this.serverConf = serverConf;
        this.gateway = gateway;
    }


    @Override
    @EventListener
    public void sendData(TcpEvent tcpEvent) {
        gateway.send(tcpEvent.getPayload(), serverConf.getClientId());
    }
}
