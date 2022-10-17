package com.pet.parser.services.implementations;


import com.pet.parser.model.Props;
import com.pet.parser.services.SaveServerPropsService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SaveServerPropsServiceImpl implements SaveServerPropsService {

    @Override
    public void saveSettings(Props props) {

        Path path = Paths.get("src/main/resources/config.properties");
        try {
            Files.write(path, ("ip=" + props.getIp() + "\nport=" + props.getPort()).getBytes(StandardCharsets.UTF_8));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
