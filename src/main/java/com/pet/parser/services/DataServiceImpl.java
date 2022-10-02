package com.pet.parser.services;



import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;



@Service
public class DataServiceImpl implements DataService{


//    private final DataRepository dataRepository;
    private final SaveService saveService;
    private final ParserService parserService;



    public DataServiceImpl(/*DataRepository dataRepository,*/ SaveService saveService, ParserService parserService) {
       // this.dataRepository = dataRepository;
        this.parserService = parserService;
        this.saveService = saveService;
    }


    @Override
    public byte[] processMessage(byte[] payload, MessageHeaders messageHeaders) {

        payload = parserService.parserPayload(payload);
        saveService.saveData(payload);
        return payload;

    }



}
