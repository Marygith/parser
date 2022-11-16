package com.pet.parser.services.implementations;

import com.pet.parser.services.SaveDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * <p>{@link SaveDataService } implementation.</p>
 * Service class that saves byte [] data to file.
 */
@Service
@PropertySource("classpath:config.properties")
public class SaveDataServiceImpl implements SaveDataService {

    @Value("${pathToFiles}")
    private String pathToFiles;


    /**
     * A method that creates file which name is a current time and saves <i>info</i> into this file.
     *
     * @param info byte [] data to save
     */
    @Override
    public void saveData(byte[] info) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");


        Date now = new Date();
        String fileName = sdf.format(now);
        Path path = Paths.get(this.pathToFiles + fileName.concat(".dat"));

        try {
            Files.write(path, info);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
