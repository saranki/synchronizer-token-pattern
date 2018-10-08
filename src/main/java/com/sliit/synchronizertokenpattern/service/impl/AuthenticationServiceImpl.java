package com.sliit.synchronizertokenpattern.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	ServerStore serverStore = new ServerStore();
	private static Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);


	/**
	 * Check if the credentials entered by the user in the Login form
	 * are same as the user credentials stored in the HashMap.
	 * Compares the username and the hash value of the typed in
	 * password.
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@Override
	public boolean isValidUser(String username, String password) throws NoSuchAlgorithmException {
		logger.info("User Authenticating...");
		return (username.equals(credentialConfiguration.getAuthUser())
				&& hashingConfiguration.convertToHash(password).equals(credentialConfiguration.getPassword()));

	}

	/**
	 * Check if the user is already authenticated using the cookies that came along with the request.
	 * If so then extract the session Id and the username from the cookies.
	 * 
	 * @param cookies
	 * @return 
	 */
	@Override
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

	/**
	 * Check if the current session Id which was extracted from the cookie 
	 * and the already stored session Id in the HashMap are the same.
	 * 
	 * @param username, sessionId
	 * @return
	 */
	@Override
	public boolean isUserSessionValid(String username, String sessionId) {
		logger.debug("Checking if user session is valid... " + username + ", " + sessionId);
		if (serverStore.findCredentials(username) != null) {
			return sessionId.equals(serverStore.findCredentials(username).getSessionId());
		}
		return false;
	}

	/**
	 * Generate a random value for session Id
	 * 
	 * @return
	 * 
	 * **/
	@Override
	public String generateSessionId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Generate CSRF token for the session using session Id
	 * 
	 * @param sessionId
	 * @return
	 * 
	 */
	@Override
	public String generateCSRFToken(String sessionId) {
		return sessionId + System.currentTimeMillis();
	}

	/**
	 * Store the session Id and the CSRF token in the server side HashMap for the particular user
	 * 
	 * @param username
	 * @return
	 * */
	@Override
	public String generateCredentialsToUser(String username) {
		User user = serverStore.findCredentials(username);
		String sessionId = generateSessionId();

		user.setSessionId(sessionId);
		user.setToken(generateCSRFToken(sessionId));
		serverStore.storeUsersAndTokens(user);

		logger.info("Storing session...");
		return sessionId;
	}

	/**
	 * Get the sessionId from the cookie that comes along with a request
	 * 
	 * @param cookies
	 * @return
	 * 
	 */
	@Override
	public String getSessionIdFromCookie(Cookie[] cookies) {
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("sessionID")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * Check if the CSRF token is valid for the session id stored in the server side
	 * 
	 * @param cookies
	 * @return
	 * 
	 */
	@Override
	public boolean isCSRFTokenValid(String sessionId, String csrfToken) {
		if (csrfToken != null) {
			logger.debug("is equal ? " + csrfToken.equals(serverStore.retrieveCSRFToken(sessionId)));
			return csrfToken.equals(serverStore.retrieveCSRFToken(sessionId));
		}
		return false;

	}

}
