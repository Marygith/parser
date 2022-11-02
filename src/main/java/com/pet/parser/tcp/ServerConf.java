package com.pet.parser.tcp;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpConnectionEvent;
import org.springframework.integration.ip.tcp.connection.TcpNioClientConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayCrLfSerializer;
import org.springframework.integration.ip.tcp.serializer.TcpCodecs;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@Configuration
@EnableIntegration
@PropertySource("classpath:config.properties")
public class ServerConf implements ApplicationListener<TcpConnectionEvent> {

    @Override
    public void onApplicationEvent(TcpConnectionEvent event) {
        clientId = event.getConnectionId();
    }


    @MessagingGateway(defaultRequestChannel = "outboundChannel")
    public interface Gateway {
        void send(@Payload byte[] data, @Header(IpHeaders.CONNECTION_ID) String connectionId);
    }

    private String clientId;

    @Value("${port}")
    private int port;

    @Value("${ip}")
    private String ip;

    public String getClientId() {
        return clientId;
    }

    @Bean
    public MessageChannel inboundChannel() {
        return new QueueChannel();
    }


    @Bean
    public MessageChannel outboundChannel() {
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
        TcpNioClientConnectionFactory tcpNioClientConnectionFactory = new TcpNioClientConnectionFactory(ip, port);
        tcpNioClientConnectionFactory.setUsingDirectBuffers(true);
        tcpNioClientConnectionFactory.setSerializer(serializer());

        tcpNioClientConnectionFactory.setSingleUse(false);
        tcpNioClientConnectionFactory.setDeserializer(deserializer());

        return tcpNioClientConnectionFactory;
    }


    @Bean
    public TcpReceivingChannelAdapter tcpReceivingChannelAdapter(MessageChannel inboundChannel, AbstractClientConnectionFactory tcpNioClientConnectionFactory) {
        TcpReceivingChannelAdapter tcpReceivingChannelAdapter = new TcpReceivingChannelAdapter();
        tcpReceivingChannelAdapter.setConnectionFactory(tcpNioClientConnectionFactory);
        tcpReceivingChannelAdapter.setClientMode(true);
        tcpReceivingChannelAdapter.setOutputChannel(inboundChannel);
        return tcpReceivingChannelAdapter;
    }


    @Bean
    @ServiceActivator(inputChannel = "outboundChannel")
    public TcpSendingMessageHandler tcpSendingMessageHandler(AbstractClientConnectionFactory tcpNioClientConnectionFactory) {
        TcpSendingMessageHandler tcpSendingMessageHandler = new TcpSendingMessageHandler();
        tcpSendingMessageHandler.setConnectionFactory(tcpNioClientConnectionFactory);
        return tcpSendingMessageHandler;
    }
}
