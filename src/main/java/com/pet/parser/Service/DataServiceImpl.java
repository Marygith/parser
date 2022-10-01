package com.pet.parser.Service;


import com.pet.parser.model.DataEntity;
import com.pet.parser.model.DataRepository;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;


@Service
public class DataServiceImpl implements DataService{


    private final DataRepository dataRepository;



    public DataServiceImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public byte[] processMessage(byte[] payload, MessageHeaders messageHeaders) {



        /*
        some parser logic here
         */

        DataEntity dataEntity = new DataEntity();
        dataEntity.setContents(payload);
        dataRepository.save(dataEntity);
        System.out.println(new String(payload));

        return payload;

    }



}
