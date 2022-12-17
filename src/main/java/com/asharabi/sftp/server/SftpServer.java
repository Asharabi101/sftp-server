package com.asharabi.sftp.server;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.config.keys.AuthorizedKeysAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class SftpServer {

	private final Log LOGGER = LogFactory.getLog(SftpServer.class);

	@PostConstruct
	public void startServer() throws IOException {
		start();
	}

	private void start() throws IOException {
		SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setHost("localhost");
		sshd.setPort(2222);

		// creating a host private key new File("host.ser").toPath()
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File("host.ser")));

		// authentication using a Public Key (file: authorized_keys)
		sshd.setPublickeyAuthenticator(
				new AuthorizedKeysAuthenticator(new File("src/main/resources/authorized_keys")));

		// adding SFTP capabilities to our SSH server
		sshd.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));

		// simple username and password based authentication
		sshd.setPasswordAuthenticator(
				(username, password, session) -> username.equals("test") && password.equals("test"));
		sshd.start();
		LOGGER.info("SFTP server started");
	}

}
