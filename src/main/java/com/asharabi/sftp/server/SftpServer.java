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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class SftpServer {

	private final Log LOGGER = LogFactory.getLog(SftpServer.class);

	@Value("${sftp.host}")
	private String sftpHost;

	@Value("${sftp.port}")
	private Integer sftpPort;

	@Value("${sftp.host.private.key.path}")
	private String sftpHostPrivateKeyPath;

	@Value("${sftp.public.key.path}")
	private String sftpPublicKeyPath;

	@Value("${sftp.user}")
	private String sftpUser;

	@Value("${sftp.password}")
	private String sftpPassword;

	@PostConstruct
	public void startServer() throws IOException, GeneralSecurityException {
		start();
	}

	private void start() throws IOException, GeneralSecurityException {
		SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setHost(sftpHost);
		sshd.setPort(sftpPort);

		// creating a host private key new File("host.ser").toPath()
		SimpleGeneratorHostKeyProvider hostKeyProvider = new SimpleGeneratorHostKeyProvider(
				new File(sftpHostPrivateKeyPath).toPath());
		sshd.setKeyPairProvider(hostKeyProvider);

		// authentication using a Public Key (file: authorized_keys)
		AuthorizedKeysAuthenticator authenticator = new AuthorizedKeysAuthenticator(
				new File(sftpPublicKeyPath).toPath());
		sshd.setPublickeyAuthenticator(authenticator);

		// adding SFTP capabilities to our SSH server
		sshd.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));

		// simple username and password based authentication
		sshd.setPasswordAuthenticator(
				(username, password, session) -> username.equals(sftpUser) && password.equals(sftpPassword));
		sshd.start();
		LOGGER.info("SFTP server started");
	}

}
