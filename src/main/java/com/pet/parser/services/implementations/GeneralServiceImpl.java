package com.pet.parser.services.implementations;


import com.pet.parser.services.GeneralService;
import com.pet.parser.services.ParserService;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;


@Service
public class GeneralServiceImpl implements GeneralService {


    private final ParserService parserServiceImpl;


    public GeneralServiceImpl(ParserServiceImpl parserServiceImpl) {

        this.parserServiceImpl = parserServiceImpl;
    }


    @Override
    public byte[] processMessage(byte[] payload, MessageHeaders messageHeaders) {

        payload = parserServiceImpl.parsePayload(payload);
        return payload;

    }


}
