package com.pet.parser.services.implementations;

import com.pet.parser.services.interfaces.ParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.Arrays;


import static com.pet.parser.services.interfaces.ParserConstants.*;

@Service
public class ParserServiceImpl implements ParserService {

    private final Logger log = LoggerFactory.getLogger(ParserServiceImpl.class);
    private ByteBuffer data;
    private ByteBuffer info;
    private ByteBuffer end;
    private short INFO_SIZE;





    @Override
    public byte [] parsePayload(byte[] payload) {

        data = ByteBuffer.allocate(MSG_DATA_SIZE);
        data.put(payload, 0, MSG_DATA_SIZE);
        parseData();

        info = ByteBuffer.allocate(INFO_SIZE);
        info.put(payload, MSG_DATA_SIZE, INFO_SIZE);

        end = ByteBuffer.allocate(MSG_DATA_SIZE);
        end.put(payload, MSG_DATA_SIZE + INFO_SIZE, MSG_DATA_SIZE);
        parseEnd();

        return info.array();
    }

    private void parseData() {
        int a_type = data.getInt(0);
        switch (a_type) {
            case TCP_IP_DATA:
                INFO_SIZE = data.getShort(MSG_SIZE_OFFSET);

                    short messageId = data.getShort(MSG_NUM_OFFSET);
                    log.info("Header received. Message number=" + messageId + "; Data " +
                            "length=" + INFO_SIZE);

                break;
            case TCP_IP_RR:
                if (log.isDebugEnabled()) {
                    log.debug("RR message received");
                }
                /*  send RR message back to server  */
                data = null;
                break;
            default:
                log.info("wrong");

        }
    }

    private void parseEnd() {
        end.clear();
        end.putInt(1);

        if(Arrays.equals(data.array(), end.array()))  {sendAckMessage();}
        else {
            /*   exception   */
        }
    }





    private void sendAckMessage()
    {
        byte [] ackMessage = end.array();
        ackMessage[0] = TCP_IP_ACK;
        /* send it */
        log.info("Data is valid");
    }





}
