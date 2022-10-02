package com.pet.parser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


import javax.net.SocketFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


@SpringBootApplication
public class ParserApplication {

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context = SpringApplication.run(ParserApplication.class, args);
		Socket socket = SocketFactory.getDefault().createSocket("localhost", 1234);
		OutputStream writer = socket.getOutputStream();
		writer.write("foo\r\n".getBytes());
		InputStream reader = socket.getInputStream();
		int resp = reader.read();
		//System.out.println(resp);
		socket.close();
		context.close();


	}


}
