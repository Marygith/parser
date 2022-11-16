package com.pet.parser.services;

import com.pet.parser.model.Props;

/**
 * Interface of service that saves {@link Props }.
 */
public interface SaveServerPropsService {

    void saveSettings(Props props);

}
