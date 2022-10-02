package com.pet.parser.services;


import com.pet.parser.model.DataEntity;
import com.pet.parser.model.DataRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SaveService {

    private final DataRepository dataRepository;



    public SaveService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }


    void saveData(byte[] payload) {
        DataEntity dataEntity = new DataEntity();
        dataEntity.setContents(payload);
        dataRepository.save(dataEntity);
        System.out.println(new String(payload));
    }


}
