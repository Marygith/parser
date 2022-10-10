package com.pet.parser.tcp;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNioClientConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayCrLfSerializer;
import org.springframework.integration.ip.tcp.serializer.TcpCodecs;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
public class ServerConf {


    @Bean
    public MessageChannel inboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public ByteArrayCrLfSerializer serializer() {

        return TcpCodecs.crlf();
    }

    @Bean
    public ByteArrayCrLfSerializer deserializer() {
        return TcpCodecs.crlf();
    }

    @Bean
    public AbstractClientConnectionFactory clientConnectionFactory() {
        TcpNioClientConnectionFactory tcpNioClientConnectionFactory = new TcpNioClientConnectionFactory("localhost", 1234);
        tcpNioClientConnectionFactory.setUsingDirectBuffers(true);
        tcpNioClientConnectionFactory.setSerializer(serializer());

        tcpNioClientConnectionFactory.setSingleUse(false);
        tcpNioClientConnectionFactory.setDeserializer(deserializer());

        return tcpNioClientConnectionFactory;
    }


    @Bean
    public TcpReceivingChannelAdapter tcpReceivingChannelAdapter(MessageChannel inboundChannel,AbstractClientConnectionFactory tcpNioClientConnectionFactory) {
        TcpReceivingChannelAdapter tcpReceivingChannelAdapter = new TcpReceivingChannelAdapter();
        tcpReceivingChannelAdapter.setConnectionFactory(tcpNioClientConnectionFactory);
        tcpReceivingChannelAdapter.setClientMode(true);
        tcpReceivingChannelAdapter.setOutputChannel(inboundChannel);



        return tcpReceivingChannelAdapter;
    }
}
