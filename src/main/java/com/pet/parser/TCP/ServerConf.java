package com.pet.parser.TCP;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.connection.TcpNioServerConnectionFactory;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
public class ServerConf {

    @Bean
    public MessageChannel inboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public TcpInboundGateway inboundGateway(MessageChannel inboundChannel) {
        TcpInboundGateway gate = new TcpInboundGateway();
        gate.setConnectionFactory( new  TcpNioServerConnectionFactory(1234));
        gate.setRequestChannel(inboundChannel);
        return gate;
    }
}
