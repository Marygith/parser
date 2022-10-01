package com.pet.parser.Service;

import org.springframework.messaging.MessageHeaders;

public interface DataService {
    byte[] processMessage(byte[] payload, MessageHeaders messageHeaders);
}
