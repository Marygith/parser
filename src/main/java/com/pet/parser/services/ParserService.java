package com.pet.parser.services;

import java.nio.ByteBuffer;

public interface ParserService {

    void parsePayload(ByteBuffer byteBuffer);

}
