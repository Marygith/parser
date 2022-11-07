package com.pet.parser.services.implementations;

import com.pet.parser.events.CustomEvent;
import com.pet.parser.services.SaveDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
@PropertySource("classpath:config.properties")
public class SaveDataServiceImpl implements SaveDataService {

    @Value("${pathToFiles}")
    private String pathToFiles;


    @EventListener(condition = "#event.eventType eq 'SaveDataEvent'")
    @Override
    public void saveData(CustomEvent event) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        byte[] info = event.getPayload();
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
