package com.asharabi.sftp.server;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.config.keys.AuthorizedKeysAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class SftpServer {

	private final Log LOGGER = LogFactory.getLog(SftpServer.class);

	@PostConstruct
	public void startServer() throws IOException, GeneralSecurityException {
		start();
	}

	private void start() throws IOException, GeneralSecurityException {
		SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setHost("localhost");
		sshd.setPort(2222);

		// creating a host private key new File("host.ser").toPath()
		SimpleGeneratorHostKeyProvider hostKeyProvider = new SimpleGeneratorHostKeyProvider(
				new File("host.ser").toPath());
		sshd.setKeyPairProvider(hostKeyProvider);

		// authentication using a Public Key (file: authorized_keys)
		AuthorizedKeysAuthenticator authenticator = new AuthorizedKeysAuthenticator(
				new File("src/main/resources/authorized_keys").toPath());
		sshd.setPublickeyAuthenticator(authenticator);

		// adding SFTP capabilities to our SSH server
		sshd.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));

		// simple username and password based authentication
		sshd.setPasswordAuthenticator(
				(username, password, session) -> username.equals("test") && password.equals("test"));
		sshd.start();
		LOGGER.info("SFTP server started");
	}

}
