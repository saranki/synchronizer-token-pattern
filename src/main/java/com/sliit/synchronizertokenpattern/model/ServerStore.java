package com.sliit.synchronizertokenpattern.model;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Saranki
 *
 */
public class ServerStore {

	public static HashMap<String, String> sessionStore = new HashMap<>();
	public static HashMap<String, User> userStore = new HashMap<>();

	private static Logger logger = LoggerFactory.getLogger(ServerStore.class);

	public void assignUserCredentials() {
		User user = new User();
		user.setUsername("master");
		user.setPassword("240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9");
		userStore.put(user.getUsername(), user);
		logger.info("Initial user credentials assigned...");
	}

	public void storeUsersAndTokens(User user) {
		userStore.put(user.getUsername(), user);
		if (user.getToken() != null) {
			sessionStore.put(user.getSessionId(), user.getToken());
		}
	}

	public User findCredentials(String username) {
		return userStore.get(username);
	}

	public String retrieveCSRFToken(String sessionId) {
		return sessionStore.get(sessionId);
	}

}
