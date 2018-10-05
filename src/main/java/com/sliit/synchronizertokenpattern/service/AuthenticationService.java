/**
 * 
 */
package com.sliit.synchronizertokenpattern.service;

import java.security.NoSuchAlgorithmException;

/**
 * @author Saranki
 *
 */
public interface AuthenticationService {
	
	public boolean isUserAuthenticated(String username, String password) throws NoSuchAlgorithmException;
	public String generateCSRFToken(String sessionId);

}
