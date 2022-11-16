package com.pet.parser.services.implementations;

import com.pet.parser.events.CustomEvent;
import com.pet.parser.events.EventsPublisherImpl;
import com.pet.parser.services.ParserService;
import com.pet.parser.tcp.ParserConstants;
import com.pet.parser.tcp.TcpHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static com.pet.parser.tcp.ParserConstants.*;

/**
 * <p>{@link ParserService } implementation.</p>
 * <p>Service class that serves as a parser for byte [] data received from server.</p>
 * <p>{@link ParserConstants} are used to define whether it's RR message
 * and if it's not - to parse <i>header</i> and <i>footer</i> from received data.
 * <i>Message</i> is parsed due to info parsed from <i>header</i>.</p>
 * <p>To save the message and reply to server {@link EventsPublisherImpl} is used.</p>
 * <p>Log4j2 is used for logging events.</p>
 */
@Service
public class ParserServiceImpl implements ParserService {


    private static final Logger log = LogManager.getLogger(ParserServiceImpl.class);

    private ByteBuffer data;
    private ByteBuffer info;
    private ByteBuffer end;
    private int state = STATE_HEADER;

    private final EventsPublisherImpl eventsPublisher;

    /**
     * Class constructor.
     *
     * @param eventsPublisher see {@link EventsPublisherImpl}.
     */
    public ParserServiceImpl(EventsPublisherImpl eventsPublisher) {

        this.eventsPublisher = eventsPublisher;
    }


    /**
     * <p>Main method that parses data. </p>Integer variable <i>state</i> indicates which part of tcp data
     * is to be parsed from {@link ByteBuffer}, that contains payload.
     *
     * @param payload received byte [] data.
     */
    @Override
    public void parsePayload(byte[] payload) {

        int len = payload.length;

        ByteBuffer buffer = ByteBuffer.allocate(len);
        buffer.put(payload);
        buffer.clear();
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

    /**
     * A method for transferring bytes from one {@link ByteBuffer} to another.
     *
     * @param p_src source ByteBuffer
     * @param p_dst destination ByteBuffer
     */
    private void fromBufToBuf(ByteBuffer p_src, ByteBuffer p_dst) {
        int a_srcRemaining = p_src.remaining();
        int a_dstRemaining = p_dst.remaining();
        int a_count = Math.min(a_srcRemaining, a_dstRemaining);
        byte[] a_bytes = new byte[a_count];
        p_src.get(a_bytes);
        p_dst.put(a_bytes);
    }


    /**
     * A method that parses header from the <i>buffer</i>.
     * <p>First byte of the header defines type of the following data.</p>
     * If it's RR message, then reply is formed and sent to the server.
     * Otherwise, message size is parsed from the header for further parsing.
     *
     * @param buffer ByteBuffer with data
     */
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


    /**
     * A method that reads message to be saved from the <i>buffer</i>.
     *
     * @param buffer ByteBuffer with data
     */
    private void readInfo(ByteBuffer buffer) {
        fromBufToBuf(buffer, info);

        if (!info.hasRemaining()) {
            state = STATE_FOOTER;
            log.info("Data message received: " + Arrays.toString(info.array()));

        }
    }


    /**
     * A method that parses footer from the <i>buffer</i>.
     * <p>If parsed header equals this footer, then data is valid.</p>
     * <p>Then message can be saved and reply to the server.</p>
     *
     * @param buffer ByteBuffer with data
     */
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

    /**
     * A method that forms acknowledgment reply for the server
     * and publishes event with this reply.
     * <p>This reply will send to the server {@link TcpHandler}.</p>
     */
    private void sendAckMessage() {
        byte[] ackMessage = end.array();
        ackMessage[0] = TCP_IP_ACK;
        eventsPublisher.publishEvent(new CustomEvent(ackMessage, "TcpEvent"));
        log.info("Data is valid");
        reset();
    }

    /**
     * A method that forms RR message for the server
     * and publishes event with this message.
     * <p>This reply will send to the server {@link TcpHandler}.</p>
     */
    private void sendRRMessage() {
        data.clear();
        byte[] a_header = new byte[MSG_DATA_SIZE];
        data.get(a_header);
        eventsPublisher.publishEvent(new CustomEvent(a_header, "TcpEvent"));
    }


    /**
     * A method that puts parser into the initial state.
     */
    private void reset() {
        data = null;
        info = null;
        end = null;
        state = STATE_HEADER;
    }
}
