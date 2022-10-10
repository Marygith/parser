package com.pet.parser.services.implementations;


import com.pet.parser.services.interfaces.DataService;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;


@Service
public class DataServiceImpl implements DataService {

    private final SaveInfo saveInfo;
    private final ParserServiceImpl parserServiceImpl;
    public byte [] payload;



    public DataServiceImpl(SaveInfo saveInfo, ParserServiceImpl parserServiceImpl) {

        this.parserServiceImpl = parserServiceImpl;
        this.saveInfo = saveInfo;
    }


    @Override
    public byte[] processMessage(byte[] payload, MessageHeaders messageHeaders) {

        this.payload = parserServiceImpl.parsePayload(payload);
        saveInfo.saveData(payload);
        return this.payload;

    }



}
