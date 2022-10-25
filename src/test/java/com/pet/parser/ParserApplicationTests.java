package com.pet.parser;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.net.ServerSocketFactory;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

@SpringBootTest
class ParserApplicationTests {


    @Test
    void contextLoads() {
        try {

            ByteBuffer byteBuffer = ByteBuffer.allocate(70);
            String buf = "qwertyuopasgdfghjk";
            String info = "some data here";
            byteBuffer.putInt(1).putInt(15).putShort((short) 14).put(buf.getBytes());
            byteBuffer.put(info.getBytes());
            byteBuffer.putInt(4).putInt(15).putShort((short) 14).put(buf.getBytes());
            byteBuffer.clear();

            Socket socket = ServerSocketFactory.getDefault().createServerSocket(1234).accept();
            socket.setKeepAlive(true);
            OutputStream writer = socket.getOutputStream();

            File file = new File("meteo");
            FileInputStream fs = new FileInputStream(file);
            byte[] b = new byte[82];
            fs.read(b, 0, 82);

            writer.write(b);
            /*byte[] aa = new byte[]{13, 10, 13, 10};
            writer.write(aa);*/
            InputStream reader = socket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(reader));
            int c;

            while ((c = in.read()) != -1) {
                StringBuilder str = new StringBuilder();
                while (c != 10) {
                    char data = (char) c;
                    str.append(data);
                    c = in.read();
                }
                System.out.println(str);
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
