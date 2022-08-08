package dev.scorza.telnetserverchat.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Set;

import dev.scorza.telnetserverchat.ChatUser;

public class TestUtils {

	private TestUtils() {
	}

	public static ChatUser mockUser(String userName, Set<ChatUser> allUsers) throws IOException {

		Socket socket = mock(Socket.class);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream inputStream = mock(InputStream.class);

		when(socket.getOutputStream()).thenReturn(outputStream);
		when(socket.getInputStream()).thenReturn(inputStream);

		return new ChatUser(socket, allUsers, userName);
	}

}
