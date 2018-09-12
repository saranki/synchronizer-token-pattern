package com.sliit.synchronizertokenpattern.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sliit.synchronizertokenpattern.util.CredentialConfiguration;
import com.sliit.synchronizertokenpattern.util.EncryptionConfiguration;

@Service
public class AuthenticationService {

	@Autowired
	CredentialConfiguration credentialConfiguration;
	
	@Autowired
	EncryptionConfiguration encryptionConfiguration;

	/*
	 * Authenticates user through username and password
	 * 
	 */
	public boolean isUserAuthenticated(String username, String password) throws NoSuchAlgorithmException {
		return (username.equals(credentialConfiguration.getAuthUser())
				&& encryptionConfiguration.convertToHash(password).equals(credentialConfiguration.getPassword()));

	}

}
