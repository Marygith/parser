package com.pet.parser.services.implementations;

import com.pet.parser.services.SaveDataService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
public class SaveDataServiceImpl implements SaveDataService {


    @Override
    public void saveData(List<byte[]> infoList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        for (byte[] info : infoList) {
            Date now = new Date();
            String fileName = sdf.format(now);
            Path path = Paths.get(fileName.concat(".dat"));

            try {
                Files.write(path, info);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
