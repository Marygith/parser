package com.pet.parser.services;


import java.util.List;

public interface ParserService {

    List<byte []> parsePayload(byte[] payload);

}
