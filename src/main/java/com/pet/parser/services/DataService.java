package com.pet.parser.services;

import org.springframework.messaging.MessageHeaders;

public interface DataService {
    byte[] processMessage(byte[] payload, MessageHeaders messageHeaders);
}
