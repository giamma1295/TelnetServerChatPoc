package dev.scorza.telnetserverchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.scorza.telnetserverchat.utils.Constants;

public class ChatUser extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChatUser.class);

	private final Socket socket;
	private final BufferedReader bufferedReader;
	private final PrintWriter printWriter;
	private String userName;

	// Shared Set of all chat users
	private Set<ChatUser> allChatUsers;

	public ChatUser(Socket socket, Set<ChatUser> allChatUsers, String userName) throws IOException {
		super();
		this.socket = socket;
		this.allChatUsers = allChatUsers;
		this.userName = userName;

		this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

	}

	@Override
	public void run() {

		welcomeUser();

		String messageContent = null;
		try {

			// Wait for new message
			while (!isCloseChatMessage(messageContent = bufferedReader.readLine())) {
				broadcastToOthers(LocalDateTime.now(), messageContent);
			}

			quitUser();

		} catch (IOException e) {
			LOGGER.error("An error has occurred while handling user operation, the reason was => {}", e.getMessage(),
					e);
		} finally {
			// Close socket and streams
			closeConnection();

			LOGGER.debug("Closed stream/socket for user => {}", userName);
		}
	}

	public void quitUser() {
		// Remove user from list
		allChatUsers.remove(this);

		// Exit Message
		sendMessage(MessageFormat.format(Constants.EXIT_MESSAGE, userName));

		LOGGER.info("User {} left the chat ", userName);

	}

	public void welcomeUser() {
		// welcome message
		sendMessage(MessageFormat.format(Constants.WELCOME_MESSAGE, userName));

		// add currentUser to user Collection
		allChatUsers.add(this);

		LOGGER.info("User {} has joined the chat", userName);

	}

	private void closeConnection() {
		try {
			if (bufferedReader != null) {
				bufferedReader.close();
			}

			if (printWriter != null) {
				printWriter.close();
			}

			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			LOGGER.error("An error has occurred while closing streams/socket, the reason was => {}", e.getMessage(), e);
		}

	}

	public void sendMessage(String message) {
		printWriter.println(message);
	}

	public void broadcastToOthers(LocalDateTime localDateTime, String messageContent) {

		var messageFormatted = MessageFormat.format(Constants.CHAT_MESSAGE_TEMPLATE, userName,
				localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), messageContent);

		allChatUsers.stream().filter(user -> this != user).forEach(user -> user.sendMessage(messageFormatted));

	}

	private boolean isCloseChatMessage(String message) {
		return StringUtils.isBlank(message) || Constants.QUIT_MESSAGES.contains(message.toLowerCase());
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

}
