package com.pet.parser.services;


import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class ParserService {


    byte [] parserPayload(byte [] payload) {
        String newPayload = new String(payload) + new String(payload);
        return newPayload.getBytes(StandardCharsets.UTF_8);
    }
}
