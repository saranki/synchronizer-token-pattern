package com.sliit.synchronizertokenpattern.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sliit.synchronizertokenpattern.service.AuthenticationService;

@Controller
public class LoginController {
	
	@Autowired
	AuthenticationService authenticationService;
	
	@GetMapping("/")
	public String goToLogin(){
		return "login";
	}
	
	@PostMapping("/home")
    public String login( HttpServletRequest servletRequest, @RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
		try {
			if(authenticationService.isUserAuthenticated(username, password)){
				System.out.println("Logged in successfully");
			    return "home.html";
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        return "redirect:/login?status=failed";
    }
}
