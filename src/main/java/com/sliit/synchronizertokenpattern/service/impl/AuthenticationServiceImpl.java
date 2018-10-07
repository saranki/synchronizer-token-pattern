package com.sliit.synchronizertokenpattern.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sliit.synchronizertokenpattern.controller.LoginController;
import com.sliit.synchronizertokenpattern.model.ServerStore;
import com.sliit.synchronizertokenpattern.model.User;
import com.sliit.synchronizertokenpattern.service.AuthenticationService;
import com.sliit.synchronizertokenpattern.util.CredentialConfiguration;
import com.sliit.synchronizertokenpattern.util.HashingConfiguration;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	CredentialConfiguration credentialConfiguration;

	@Autowired
	HashingConfiguration hashingConfiguration;
	private static Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	ServerStore serverStore = new ServerStore();

	/*
	 * Validate user if he is already registered with the system through
	 * username and password
	 * 
	 */
	@Override
	public boolean isValidUser(String username, String password) throws NoSuchAlgorithmException {
		logger.info("User Authenticating...");
		return (username.equals(credentialConfiguration.getAuthUser())
				&& hashingConfiguration.convertToHash(password).equals(credentialConfiguration.getPassword()));

	}

	/*
	 * Authenticate user with session ID and username
	 */
	public boolean isUserAuthenticated(Cookie[] cookies) {
		String username = "";
		String sessionID = "";

		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("sessionID")) {
					sessionID = cookie.getValue();
				} else if (cookie.getName().equals("username")) {
					username = cookie.getValue();
				}
			}
		}

		return isUserSessionValid(username, sessionID);
	}

	/*
	 * Validate whether the current session id and the already stored session Id
	 * are the same
	 */
	public boolean isUserSessionValid(String username, String sessionId) {
		logger.debug("Checking if user session is valid... " + username + ", " + sessionId);
		if (serverStore.findCredentials(username) != null) {
			// logger.debug("serverStore.findCredentials(username) -> " +
			// serverStore.findCredentials(username));
			return sessionId.equals(serverStore.findCredentials(username).getSessionId());
		}
		return false;
	}

	// generate session ID
	public String generateSessionId() {
		return UUID.randomUUID().toString();
	}

	/*
	 * Generate CSRF token and store it in the server side.
	 * 
	 */
	@Override
	public String generateCSRFToken(String sessionId) {
		return sessionId + System.currentTimeMillis();
	}

	public String generateCredentialsToUser(String username) {
		User user = serverStore.findCredentials(username);
		String sessionId = generateSessionId();

		user.setSessionId(sessionId);
		user.setToken(generateCSRFToken(sessionId));
		serverStore.storeUsersAndTokens(user);

		logger.info("Storing session...");
		return sessionId;
	}

	/*
	 * Get the sessionId from the cookie
	 * 
	 */
	public String getSessionIdFromCookie(Cookie[] cookies) {
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				// if
				// (cookie.getName().equals(credentialConfiguration.getAuthUser())){
				if (cookie.getName().equals("sessionID")) {
					// logger.info("cookie value: " + cookie.getValue());
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public boolean isCSRFTokenValid(String sessionId, String csrfToken) {
		logger.debug("XXX -> " + sessionId + ", " + csrfToken);

		if (csrfToken != null) {
			logger.debug("is equal ? " + csrfToken.equals(serverStore.retrieveCSRFToken(sessionId)));
			return csrfToken.equals(serverStore.retrieveCSRFToken(sessionId));
		}
		return false;

	}

}
