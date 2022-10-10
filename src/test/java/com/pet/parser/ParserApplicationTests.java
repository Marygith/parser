package com.pet.parser;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.net.ServerSocketFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;


@SpringBootTest
class ParserApplicationTests {




	@Test
	void contextLoads() {
		try {

			ByteBuffer byteBuffer = ByteBuffer.allocate(70);
			String buf = "qwertyuiopasdfghjk";
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

			int resp = reader.read();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
