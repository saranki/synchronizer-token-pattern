/**
 * 
 */
package com.sliit.synchronizertokenpattern.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sliit.synchronizertokenpattern.model.ServerStore;
import com.sliit.synchronizertokenpattern.service.impl.AuthenticationServiceImpl;

/**
 * @author Saranki
 *
 */
@RestController
public class TokenController {
	private Logger logger = LoggerFactory.getLogger(TokenController.class);

	@Autowired
	AuthenticationServiceImpl authenticationService;

	@GetMapping(path = "/token")
	public String token(HttpServletRequest request) {
		logger.debug("Requesting Token...");
		String sessionId = authenticationService.getSessionIdFromCookie(request.getCookies());
		String token = "";
		if (sessionId != null) {
			token = new ServerStore().retrieveCSRFToken(sessionId);
			if (token != null) {
				logger.info("Successfully generated token!");
				return token;
			}
		}
		logger.error("User Authentication failed!");
		return token;
	}

}
