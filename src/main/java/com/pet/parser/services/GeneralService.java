package com.pet.parser.services;

import org.springframework.messaging.MessageHeaders;

public interface GeneralService {

    byte[] processMessage(byte[] payload, MessageHeaders messageHeaders);

}
