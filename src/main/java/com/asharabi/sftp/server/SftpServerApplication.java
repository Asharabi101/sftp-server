package com.asharabi.sftp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SftpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SftpServerApplication.class, args);
		
		// to keep running and listen to the incoming connections
		while (true);
	}

}
