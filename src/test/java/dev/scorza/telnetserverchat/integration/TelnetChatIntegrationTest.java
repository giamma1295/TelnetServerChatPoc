package dev.scorza.telnetserverchat.integration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import dev.scorza.telnetserverchat.ChatUser;
import dev.scorza.telnetserverchat.utils.Constants;
import dev.scorza.telnetserverchat.utils.TestUtils;

public class TelnetChatIntegrationTest {

	@Test
	public void TEST_RECEIVE_AND_BROADCAST_MESSAGE_FLOW() throws IOException {

		Set<ChatUser> allUsers = Collections.synchronizedSet(new HashSet<ChatUser>());

		// mock receiver user
		var user2 = TestUtils.mockUser("USER_2", allUsers);
		user2.welcomeUser();

		var user3 = TestUtils.mockUser("USER_3", allUsers);
		user3.welcomeUser();

		// Mock sender
		Socket socket = mock(Socket.class);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		// Send 2 messages, the first one which will be brodcasted to other users, and
		// the second one which will quit the user
		ByteArrayInputStream inputStream = new ByteArrayInputStream("test message from user1\nquit\n".getBytes());

		when(socket.getOutputStream()).thenReturn(outputStream);
		when(socket.getInputStream()).thenReturn(inputStream);

		ChatUser chatUser = new ChatUser(socket, allUsers, "USER_1");
		chatUser.run();

		// sender should not have the message
		Assert.assertFalse(chatUser.getSocket().getOutputStream().toString().contains("test message from user1"));

		// receiver users should have the sender message
		Assert.assertTrue(user2.getSocket().getOutputStream().toString().contains("test message from user1"));
		Assert.assertTrue(user3.getSocket().getOutputStream().toString().contains("test message from user1"));

	}

	@Test
	public void TEST_QUIT_MESSAGE_RECEIVED_FLOW() throws IOException {
		Set<ChatUser> allUsers = Collections.synchronizedSet(new HashSet<ChatUser>());
		// Mock sender
		Socket socket = mock(Socket.class);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		// Send 2 messages, the first one which will be brodcasted to other users, and
		// the second one which will quit the user
		ByteArrayInputStream inputStream = new ByteArrayInputStream("test message from user1\nquit\n".getBytes());

		when(socket.getOutputStream()).thenReturn(outputStream);
		when(socket.getInputStream()).thenReturn(inputStream);

		ChatUser chatUser = new ChatUser(socket, allUsers, "USER_1");
		chatUser.run();

		// on user output stream we should found Welcome message and quit message
		Assert.assertTrue(chatUser.getSocket().getOutputStream().toString()
				.contains(MessageFormat.format(Constants.WELCOME_MESSAGE, "USER_1")));
		Assert.assertTrue(chatUser.getSocket().getOutputStream().toString()
				.contains(MessageFormat.format(Constants.EXIT_MESSAGE, "USER_1")));

		// and allUsers should be empty cause the only user connect has left the chat
		Assert.assertTrue(allUsers.isEmpty());

	}

}
