package com.pet.parser.services.implementations;

import com.pet.parser.services.interfaces.SaveService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class SaveInfo implements SaveService{


    @Override
    public void saveData(byte[] payload) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date now = new Date();
        String fileName = sdf.format(now);
        Path path = Paths.get(fileName.concat(".dat"));
        try {
            Files.write(path, payload);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
