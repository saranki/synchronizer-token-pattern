/**
 * 
 */
package com.sliit.synchronizertokenpattern.service;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;

/**
 * @author Saranki
 *
 */
public interface AuthenticationService {
	
	public boolean isValidUser(String username, String password) throws NoSuchAlgorithmException;
	public String generateSessionId();
	public String generateCSRFToken(String sessionId);
	public boolean isUserAuthenticated(Cookie[] cookies);
	public boolean isUserSessionValid(String username, String sessionId);
	public String generateCredentialsToUser(String username);
	public String getSessionIdFromCookie(Cookie[] cookies);
	public boolean isCSRFTokenValid(String sessionId, String csrfToken);
	
}
