package com.pet.parser.services.implementations;

import com.pet.parser.events.CustomEvent;
import com.pet.parser.events.EventsPublisher;
import com.pet.parser.services.ParserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static com.pet.parser.tcp.ParserConstants.*;

@Service
public class ParserServiceImpl implements ParserService {


    private static final Logger log = LogManager.getLogger(ParserServiceImpl.class);

    private ByteBuffer data;
    private ByteBuffer info;
    private ByteBuffer end;
    private int state = STATE_HEADER;

    private final EventsPublisher eventsPublisher;

    public ParserServiceImpl(EventsPublisher eventsPublisher) {

        this.eventsPublisher = eventsPublisher;
    }


    @Override
    @EventListener
    public void parsePayload(ByteBuffer buffer) {

        buffer.clear();

        while (buffer.remaining() > 0) {
            switch (state) {
                case STATE_HEADER:

                    readData(buffer);
                    break;
                case STATE_DATA:
                    readInfo(buffer);
                    break;
                case STATE_FOOTER:
                    readEnd(buffer);

            }
        }

    }

    private void fromBufToBuf(ByteBuffer p_src, ByteBuffer p_dst) {
        int a_srcRemaining = p_src.remaining();
        int a_dstRemaining = p_dst.remaining();
        int a_count = Math.min(a_srcRemaining, a_dstRemaining);
        byte[] a_bytes = new byte[a_count];
        p_src.get(a_bytes);
        p_dst.put(a_bytes);
    }

    private void readData(ByteBuffer buffer) {

        if (data == null) {
            data = ByteBuffer.allocate(MSG_DATA_SIZE);
        }
        fromBufToBuf(buffer, data);

        if (!data.hasRemaining()) {
            int a_type = data.get(0);
            switch (a_type) {
                case TCP_IP_DATA:
                    short INFO_SIZE = data.getShort(MSG_SIZE_OFFSET);

                    info = ByteBuffer.allocate(INFO_SIZE);
                    short messageId = data.getShort(MSG_NUM_OFFSET);
                    log.info("Header received. Message number=" + messageId + "; Data " +
                            "length=" + INFO_SIZE);
                    state = STATE_DATA;

                    break;
                case TCP_IP_RR:

                    log.info("RR message received");
                    sendRRMessage();
                    data = null;
                    break;

                default:
                    log.warn("Wrong message type");
            }
        }
    }


    private void readInfo(ByteBuffer buffer) {
        fromBufToBuf(buffer, info);

        if (!info.hasRemaining()) {
            state = STATE_FOOTER;
            log.info("Data message received: " + Arrays.toString(info.array()));

        }
    }


    private void readEnd(ByteBuffer buffer) {
        if (end == null) {
            end = ByteBuffer.allocate(MSG_DATA_SIZE);
        }
        fromBufToBuf(buffer, end);

        if (!end.hasRemaining()) {
            byte[] a_end = end.array();
            byte[] a_data = data.array();

            a_data[0] = TCP_IP_END;
            if (Arrays.equals(a_data, a_end)) {
                eventsPublisher.publishEvent(new CustomEvent(info.array(), "SaveDataEvent"));
                sendAckMessage();
            }
        }
    }

    private void sendAckMessage() {
        byte[] ackMessage = end.array();
        ackMessage[0] = TCP_IP_ACK;
        eventsPublisher.publishEvent(new CustomEvent(ackMessage, "TcpEvent"));
        log.info("Data is valid");
        reset();
    }

    private void sendRRMessage() {
        data.clear();
        byte[] a_header = new byte[MSG_DATA_SIZE];
        data.get(a_header);
        eventsPublisher.publishEvent(new CustomEvent(a_header, "TcpEvent"));
    }


    private void reset() {
        data = null;
        info = null;
        end = null;
        state = STATE_HEADER;
    }
}
