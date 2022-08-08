package dev.scorza.telnetserverchat.utils;

import java.util.Arrays;
import java.util.List;

public class Constants {

	private Constants() {
	}

	public static final String WELCOME_MESSAGE = "Hi {0}! Welcome to this chat :)! Feel free to share with others what do you want No rule here!";
	public static final String EXIT_MESSAGE = "See you later {0}, Bye Bye! :) XoXo";
	public static final String CHAT_MESSAGE_TEMPLATE = "[{0}] {1} => {2}";

	public static final String USER_NAME_TEMPLATE = "User_{0}";

	public static final List<String> QUIT_MESSAGES = Arrays.asList("quit", "q", "exit", "leave");

}
