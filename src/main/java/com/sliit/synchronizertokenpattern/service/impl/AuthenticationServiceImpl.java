package com.sliit.synchronizertokenpattern.service.impl;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sliit.synchronizertokenpattern.model.ServerStore;
import com.sliit.synchronizertokenpattern.service.AuthenticationService;
import com.sliit.synchronizertokenpattern.util.CredentialConfiguration;
import com.sliit.synchronizertokenpattern.util.EncryptionConfiguration;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

	@Autowired
	CredentialConfiguration credentialConfiguration;
	
	@Autowired
	EncryptionConfiguration encryptionConfiguration;
	


	/*
	 * Authenticates user through username and password
	 * 
	 */
	@Override
	public boolean isUserAuthenticated(String username, String password) throws NoSuchAlgorithmException {
		return (username.equals(credentialConfiguration.getAuthUser())
				&& encryptionConfiguration.convertToHash(password).equals(credentialConfiguration.getPassword()));

	}
	
	/*
	 * Generate CSRF token and store it in the server side.
	 * 
	 * */
	@Override
	public String generateCSRFToken(String sessionId){
		
		String csrfToken = sessionId + System.currentTimeMillis();
		
		// Store sessionID in Server side
		ServerStore.getUserSessionDetails().storeCSRFToken(sessionId, csrfToken);
		return csrfToken;
	}
	
	/*
	 * Get the sessionId from the cookie
	 * 
	 * */
	public String getSessionIdFromCookie(Cookie[] cookies){
        if (cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies) {
            	System.out.println("Session ID identifier: "+credentialConfiguration.getAuthUser().toString());
                if (cookie.getName().equals(credentialConfiguration.getAuthUser())){
                	System.out.println("cookie value: "+cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
	
	public boolean isCSRFTokenValid(String sessionId, String csrfToken){
		
		if(csrfToken != null){
			return csrfToken.equals(ServerStore.getUserSessionDetails().retrieveCSRFToken(sessionId));
		}		
		return false;
		
	}

}
