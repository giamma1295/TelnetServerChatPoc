package dev.scorza.telnetserverchat.unit;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import dev.scorza.telnetserverchat.ChatUser;
import dev.scorza.telnetserverchat.utils.Constants;
import dev.scorza.telnetserverchat.utils.TestUtils;

public class ChatUserUnitTest {

	@Test
	public void TEST_SEND_MESSAGE() throws IOException {

		var message = "prova";

		Set<ChatUser> allUsers = Collections.synchronizedSet(new HashSet<ChatUser>());

		ChatUser chatUser = TestUtils.mockUser("USER_1", allUsers);
		chatUser.sendMessage(message);

		Assert.assertEquals(message.concat("\n"), chatUser.getSocket().getOutputStream().toString());

	}

	@Test
	public void TEST_WELCOME_USER() throws IOException {

		Set<ChatUser> allUsers = Collections.synchronizedSet(new HashSet<ChatUser>());

		// Adding 3 user and welcoming them
		var user1 = TestUtils.mockUser("USER_1", allUsers);
		user1.welcomeUser();

		var user2 = TestUtils.mockUser("USER_2", allUsers);
		user2.welcomeUser();

		var user3 = TestUtils.mockUser("USER_3", allUsers);
		user3.welcomeUser();

		// should we have 3 user in set
		Assert.assertEquals(3, allUsers.size());

		// all users should receive the welcome message
		Assert.assertEquals(MessageFormat.format(Constants.WELCOME_MESSAGE, "USER_1").concat("\n"),
				user1.getSocket().getOutputStream().toString());
		Assert.assertEquals(MessageFormat.format(Constants.WELCOME_MESSAGE, "USER_2").concat("\n"),
				user2.getSocket().getOutputStream().toString());
		Assert.assertEquals(MessageFormat.format(Constants.WELCOME_MESSAGE, "USER_3").concat("\n"),
				user3.getSocket().getOutputStream().toString());

	}

	@Test
	public void TEST_QUIT_USER() throws IOException {
		Set<ChatUser> allUsers = Collections.synchronizedSet(new HashSet<ChatUser>());

		// Adding 2 user and welcoming them
		var user1 = TestUtils.mockUser("USER_1", allUsers);
		user1.welcomeUser();

		var user2 = TestUtils.mockUser("USER_2", allUsers);
		user2.welcomeUser();

		// Quit user1
		user1.quitUser();

		// should we have 1 user in set
		Assert.assertEquals(1, allUsers.size());

		// user1 should not be in set
		Assert.assertFalse(allUsers.contains(user1));

		// user2 should be in set
		Assert.assertTrue(allUsers.contains(user2));

	}

	@Test
	public void TEST_BROADCAST_TO_OTHERS() throws IOException {
		Set<ChatUser> allUsers = Collections.synchronizedSet(new HashSet<ChatUser>());

		var messageContent = "prova";
		var localDateTime = LocalDateTime.now();
		var sentMessage = MessageFormat.format(Constants.CHAT_MESSAGE_TEMPLATE, "USER_1",
				localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), messageContent);

		// Adding 3 user and welcoming them
		var user1 = TestUtils.mockUser("USER_1", allUsers);
		user1.welcomeUser();

		var user2 = TestUtils.mockUser("USER_2", allUsers);
		user2.welcomeUser();

		var user3 = TestUtils.mockUser("USER_3", allUsers);
		user3.welcomeUser();

		// user1 broadcast message
		user1.broadcastToOthers(localDateTime, messageContent);

		// user1 should not have message
		Assert.assertFalse(user1.getSocket().getOutputStream().toString().contains(sentMessage));

		// user2 and 3 should have the message
		Assert.assertTrue(user2.getSocket().getOutputStream().toString().contains(sentMessage));
		Assert.assertTrue(user3.getSocket().getOutputStream().toString().contains(sentMessage));
	}
}
