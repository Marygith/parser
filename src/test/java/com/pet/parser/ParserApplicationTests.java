package com.pet.parser;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.net.ServerSocketFactory;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

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

            OutputStream writer = socket.getOutputStream();
            writer.write(byteBuffer.array());
            writer.write("\r\n".getBytes());
            InputStream reader = socket.getInputStream();


            BufferedReader in = new BufferedReader(new InputStreamReader(reader));
            int c;
            StringBuilder str = new StringBuilder();
            while ((c = in.read()) != -1) {
                char data = (char) c;
                str.append(data);
                if (c == 10) {
                    break;
                }
            }

            System.out.println(str);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
