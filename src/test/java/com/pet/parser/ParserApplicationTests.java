package com.pet.parser;

import com.pet.parser.services.implementations.ParserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.net.ServerSocketFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ParserApplicationTests {

    private static final Logger log = LogManager.getLogger(ParserServiceImpl.class);


    @Test
    void
    contextLoads() {

        try {
            Socket socket = ServerSocketFactory.getDefault().createServerSocket(1234).accept();
            OutputStream writer = socket.getOutputStream();

            Path path = Paths.get("meteo");
            byte[] dataFromFile = Files.readAllBytes(path);
            writer.write(dataFromFile);

            InputStream reader = socket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(reader));
            int c;

            while ((c = in.read()) != -1) {
                List<Byte> ans = new ArrayList<>();

                while (c != 10) {
                    ans.add((byte) c);
                    c = in.read();
                }
                log.info("Answer: " + Arrays.toString(ans.toArray()));
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}




