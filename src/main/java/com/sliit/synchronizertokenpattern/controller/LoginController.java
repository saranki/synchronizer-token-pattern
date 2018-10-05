package com.sliit.synchronizertokenpattern.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sliit.synchronizertokenpattern.service.impl.AuthenticationServiceImpl;
import com.sliit.synchronizertokenpattern.util.EncryptionConfiguration;

@Controller
public class LoginController {
	
	@Autowired
	AuthenticationServiceImpl authenticationServiceImpl;
	
	@Autowired
	EncryptionConfiguration encryptionConfiguration;

	private HttpSession newSession;
	
	
	@GetMapping("/")
	public String goToLogin(){
		return "login";
	}
	
	
	
	@PostMapping("/home")
//    public String login( HttpServletRequest servletRequest, HttpServletResponse servletResponse, @RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
    public String login( HttpServletRequest servletRequest, HttpServletResponse servletResponse, @RequestParam(value = "username", required = true) String username, @RequestParam(value = "password", required = true) String password) {
	
	try {
			System.out.println("Username "+username);
			System.out.println("Password "+password);
			if(authenticationServiceImpl.isUserAuthenticated(username, password)){
				String sessionID = null;
				// Invalidate previous session
				HttpSession oldSession = servletRequest.getSession(false);
	            if (oldSession != null) {
	            	System.out.println("Old session");
	                oldSession.invalidate();
	            }
	            
	            if(sessionID==null){
	            	HttpSession newSession = servletRequest.getSession(true);
		            // Generate session ID
		            sessionID = encryptionConfiguration.generateSessionId();
		            
		            // Set it in the set-cookie header
		            Cookie cookie = new Cookie(username, sessionID);
		            servletResponse.addCookie(cookie);
		            String tokenGenerate = authenticationServiceImpl.generateCSRFToken(sessionID);
		            
		            System.out.println("Server generated CSRF token:-------->"+ tokenGenerate);
		            System.out.println("Cookie: "+cookie.getValue().toString()+ "n/Cookie name: "+cookie.getName().toString());
	            }
	            
	            
	            System.out.println("new session :"+ encryptionConfiguration.generateSessionId());
				System.out.println("Logged in successfully");
			    return "home.html";
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        return "redirect:/login?status=failed";
    }
}
