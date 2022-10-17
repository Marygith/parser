package com.pet.parser.tcp;

public class ParserConstants {

    public static final int TCP_IP_DATA = 1;
    public static final int TCP_IP_ACK = 2;
    public static final int TCP_IP_RR = 6;


    public static final int MSG_DATA_SIZE = 28;
    public static final int MSG_SIZE_OFFSET = 8;
    public static final int MSG_NUM_OFFSET = 10;

    public static final int STATE_HEADER = 1;
    public static final int STATE_DATA = 2;
    public static final int STATE_FOOTER = 3;


}
