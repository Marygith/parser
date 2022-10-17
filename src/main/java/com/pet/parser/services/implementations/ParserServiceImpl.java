package com.pet.parser.services.implementations;

import com.pet.parser.services.ParserService;
import com.pet.parser.services.SaveDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.Arrays;


import static com.pet.parser.tcp.ParserConstants.*;

@Service
public class ParserServiceImpl implements ParserService {

    private final SaveDataService saveData;

    private final Logger log = LoggerFactory.getLogger(ParserServiceImpl.class);
    private ByteBuffer data;
    private ByteBuffer info;
    private ByteBuffer end;
    private int state = STATE_HEADER;
    private boolean isRR = false;

    public ParserServiceImpl(SaveDataService saveData) {
        this.saveData = saveData;
    }


    @Override
    public byte[] parsePayload(byte[] payload) {

        int len = payload.length;
        ByteBuffer buffer = ByteBuffer.allocate(len);
        buffer.put(payload);

        buffer.clear();
        while (buffer.remaining() > 0) {
            switch (state) {
                case STATE_HEADER:
                    readData(buffer);
                    if (isRR) {
                        byte[] response = data.array();
                        data = null;
                        return response;
                    }
                    break;
                case STATE_DATA:
                    readInfo(buffer);
                    payload = info.array();
                    break;
                case STATE_FOOTER:
                    byte[] response = readEnd(buffer);
                    if (response != null) {
                        return response;
                    } else {
                        return "".getBytes();
                    }


            }
        }


        return payload;
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

        data = ByteBuffer.allocate(MSG_DATA_SIZE);
        fromBufToBuf(buffer, data);
        data.clear();

        int a_type = data.getInt(0);
        switch (a_type) {
            case TCP_IP_DATA:
                short INFO_SIZE = data.getShort(MSG_SIZE_OFFSET);
                info = ByteBuffer.allocate(INFO_SIZE);
                short messageId = data.getShort(MSG_NUM_OFFSET);
                log.debug("Header received. Message number=" + messageId + "; Data " +
                        "length=" + INFO_SIZE);
                state = STATE_DATA;

                break;
            case TCP_IP_RR:
                if (log.isDebugEnabled()) {
                    log.debug("RR message received");
                }
                isRR = true;

                break;
            default:
                log.debug("Wrong message type");

        }
    }


    private void readInfo(ByteBuffer buffer) {
        fromBufToBuf(buffer, info);
        if (!info.hasRemaining()) {
            state = STATE_FOOTER;
            if (log.isDebugEnabled()) {
                log.debug("Data message received");
            }
        }
    }


    private byte[] readEnd(ByteBuffer buffer) {
        end = ByteBuffer.allocate(MSG_DATA_SIZE);
        byte[] ackMessage = null;
        fromBufToBuf(buffer, end);

        end.clear();
        end.putInt(1);


        if (Arrays.equals(data.array(), end.array())) {

            ackMessage = end.array();
            ackMessage[0] = TCP_IP_ACK;
            saveData.saveData(info.array());
            log.info("Data is valid");
            reset();
        }
        return ackMessage;
    }


    private void reset() {
        data = null;
        info = null;
        end = null;
        state = STATE_HEADER;
    }


}
