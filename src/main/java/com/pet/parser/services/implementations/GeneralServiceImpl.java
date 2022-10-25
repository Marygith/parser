package com.pet.parser.services.implementations;


import com.pet.parser.services.GeneralService;
import com.pet.parser.services.ParserService;
import com.pet.parser.services.SaveDataService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GeneralServiceImpl implements GeneralService {


    private final ParserService parserServiceImpl;
    private final SaveDataService saveDataServiceImpl;


    public GeneralServiceImpl(ParserServiceImpl parserServiceImpl, SaveDataService saveDataServiceImpl) {

        this.parserServiceImpl = parserServiceImpl;
        this.saveDataServiceImpl = saveDataServiceImpl;
    }


    @Override
    public void  processMessage(byte[] payload) {

        List<byte []> list = parserServiceImpl.parsePayload(payload);
        saveDataServiceImpl.saveData(list);

    }



}
