package com.pet.parser.services.implementations;


import com.pet.parser.model.DataEntity;
import com.pet.parser.model.DataRepository;
import com.pet.parser.services.interfaces.SaveService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SaveServiceImpl implements SaveService {

    private final DataRepository dataRepository;



    public SaveServiceImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }


    @Override
    public void saveData(byte[] payload) {
        DataEntity dataEntity = new DataEntity();
        dataEntity.setContents(payload);
        dataRepository.save(dataEntity);
        System.out.println(new String(payload));
    }


}
