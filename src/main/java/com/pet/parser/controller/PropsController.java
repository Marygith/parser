package com.pet.parser.controller;

import com.pet.parser.model.Props;
import com.pet.parser.services.SaveServerPropsService;
import com.pet.parser.services.implementations.SaveServerPropsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/config")
public class PropsController {

    private final SaveServerPropsService saveServerPropsService;

    public PropsController(SaveServerPropsServiceImpl saveServerSettingsService) {
        this.saveServerPropsService = saveServerSettingsService;
    }

    @PostMapping
    public ResponseEntity<?> getServerSettings(@RequestBody Props props) {

        saveServerPropsService.saveSettings(props);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
