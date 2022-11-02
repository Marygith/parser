package com.pet.parser.services.implementations;


import com.pet.parser.services.GeneralService;
import com.pet.parser.services.ParserService;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;


@Service
public class GeneralServiceImpl implements GeneralService {


    private final ParserService parserServiceImpl;


    public GeneralServiceImpl(ParserServiceImpl parserServiceImpl) {

        this.parserServiceImpl = parserServiceImpl;

    }


    @Override
    public void processMessage(ByteBuffer byteBuffer) {


        parserServiceImpl.parsePayload(byteBuffer);

    }
}
