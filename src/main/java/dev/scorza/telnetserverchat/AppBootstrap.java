package dev.scorza.telnetserverchat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppBootstrap {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppBootstrap.class);

	public static void main(String[] args) {
		try {
			// Default Port
			int serverPort = 10000;

			// port as first argument
			if (args.length > 0 && StringUtils.isNumeric(args[0])) {
				serverPort = Integer.parseInt(args[0]);
			}

			new TelnetServerChat(serverPort).start();

			LOGGER.info("TelnetServerChat sucessfully started on port {}", serverPort);
		} catch (Exception e) {
			LOGGER.error("Error starting TelnetServerChat, the reason was => {}", e.getMessage(), e);
		}

	}

}
