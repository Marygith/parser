package com.pet.parser.services.implementations;


import com.pet.parser.model.Props;
import com.pet.parser.services.SaveServerPropsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * <p>{@link SaveServerPropsService } implementation.</p>
 * Service class that saves ip and port from received {@link Props} to the config.properties.
 */
@Service
@PropertySource("classpath:config.properties")
public class SaveServerPropsServiceImpl implements SaveServerPropsService {

    @Value("${pathToFiles}")
    private String pathToFiles;

    /**
     * A method that saves Props to the file.
     *
     * @param props see {@link Props}
     */
    @Override
    public void saveSettings(Props props) {

        Path path = Paths.get("src/main/resources/config.properties");
        try {
            Files.write(path, ("pathToFiles=" + pathToFiles + "\nip=" + props.getIp() + "\nport=" + props.getPort()).getBytes(StandardCharsets.UTF_8));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
