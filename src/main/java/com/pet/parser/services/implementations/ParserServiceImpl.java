package com.pet.parser.services.implementations;

import com.pet.parser.services.ParserService;
import com.pet.parser.tcp.ServerConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static com.pet.parser.tcp.ParserConstants.*;

@Service
public class ParserServiceImpl implements ParserService {

    List<byte[]> infoList = new ArrayList<>();

    private final ServerConf serverConf;
    private final ServerConf.Gateway gateway;
    private final Logger log = LoggerFactory.getLogger(ParserServiceImpl.class);
    private ByteBuffer data;
    private ByteBuffer info;
    private ByteBuffer end;
    private int state = STATE_HEADER;
    private boolean isRR = false;
    String clientId;

    public ParserServiceImpl(ServerConf serverConf, ServerConf.Gateway gateway) {
        this.serverConf = serverConf;
        this.gateway = gateway;
    }


    private void send(byte[] data, String clientId) {
        gateway.send(data, clientId);
    }

    @Override
    public List<byte[]> parsePayload(byte[] payload) {

       // System.out.println(Arrays.toString(payload));

        clientId = serverConf.getClientId();
        int len = payload.length;
        ByteBuffer buffer = ByteBuffer.allocate(len);
        buffer.put(payload);

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
        return infoList;
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
        data.clear();

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
                    if (log.isDebugEnabled()) {
                        log.debug("RR message received");
                    }
                    sendRRMessage();
                    data = null;
                    break;

                default:
                    log.info("Wrong message type");

            }
        }
    }


    private void readInfo(ByteBuffer buffer) {
        fromBufToBuf(buffer, info);

        if (!info.hasRemaining()) {
            state = STATE_FOOTER;
            if (log.isDebugEnabled()) {
                log.info("Data message received: " + Arrays.toString(info.array()));
            }
        }
    }


    private void readEnd(ByteBuffer buffer) {
        end = ByteBuffer.allocate(MSG_DATA_SIZE);

        fromBufToBuf(buffer, end);
        end.clear();
        end.putInt(1);


        if (Arrays.equals(data.array(), end.array())) {
            infoList.add(info.array());
            sendAckMessage();
        }
    }

    private void sendAckMessage() {
        byte[] ackMessage = end.array();
        ackMessage[0] = TCP_IP_ACK;
        send(ackMessage, clientId);
        log.info("Data is valid");
        reset();
    }

    private void sendRRMessage() {
        data.clear();
        byte[] a_header = new byte[MSG_DATA_SIZE];
        data.get(a_header);
        send(a_header, clientId);
    }


    private void reset() {
        data = null;
        info = null;
        end = null;
        state = STATE_HEADER;
    }


}
