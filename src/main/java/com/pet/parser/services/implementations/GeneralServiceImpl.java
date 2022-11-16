package com.pet.parser.services.implementations;


import com.pet.parser.services.GeneralService;
import com.pet.parser.services.ParserService;
import org.springframework.stereotype.Service;

/**
 * <p>GeneralService implementation.</p>
 * Service class that processes byte [] data received from server,
 * passing it to the {@link ParserService}.
 */

@Service
public class GeneralServiceImpl implements GeneralService {


    private final ParserService parserServiceImpl;


    public GeneralServiceImpl(ParserServiceImpl parserServiceImpl) {

        this.parserServiceImpl = parserServiceImpl;

    }


    /**
     * A method that passes data to the {@link ParserService}.
     *
     * @param payload received data.
     */
    @Override
    public void processMessage(byte[] payload) {


        parserServiceImpl.parsePayload(payload);

    }
}
