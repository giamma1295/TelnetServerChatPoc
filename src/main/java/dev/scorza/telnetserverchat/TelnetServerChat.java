package dev.scorza.telnetserverchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.scorza.telnetserverchat.utils.Constants;

public class TelnetServerChat extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChatUser.class);

	private ServerSocket serverSocket;
	private final Set<ChatUser> allUsers;

	public TelnetServerChat(int serverPort) throws IOException {
		super();
		this.serverSocket = new ServerSocket(serverPort);
		this.allUsers = Collections.synchronizedSet(new HashSet<ChatUser>());
	}

	@Override
	public void run() {

		var userId = 1;

		try {
			while (true) {

				ChatUser chatUser = new ChatUser(serverSocket.accept(), allUsers,
						MessageFormat.format(Constants.USER_NAME_TEMPLATE, userId));

				chatUser.start();

				userId++;

			}
		} catch (IOException e) {
			LOGGER.error("An error has occurred while accepting user, the reason was => {}", e.getMessage(), e);
		} finally {
			closeServerSocket();
		}
	}

	private void closeServerSocket() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			LOGGER.error("An error has occurred while closing serverSocket, the reason was => {}", e.getMessage(), e);
		}

	}

}
