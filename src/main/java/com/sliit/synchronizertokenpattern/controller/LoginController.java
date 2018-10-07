package com.sliit.synchronizertokenpattern.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sliit.synchronizertokenpattern.model.ServerStore;
import com.sliit.synchronizertokenpattern.model.Transfer;
import com.sliit.synchronizertokenpattern.model.User;
import com.sliit.synchronizertokenpattern.service.impl.AuthenticationServiceImpl;
import com.sliit.synchronizertokenpattern.util.HashingConfiguration;

@Controller
public class LoginController {

	@Autowired
	AuthenticationServiceImpl authenticationServiceImpl;

	@Autowired
	HashingConfiguration hashingConfiguration;

	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@GetMapping("/")
	public String loadPage(Model model, HttpServletRequest request) {
		if (!authenticationServiceImpl.isUserAuthenticated(request.getCookies())) {
			return "redirect:/login";
		} else {
			return "redirect:/transfer";
		}
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("login", new User());
		return "login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute User user, HttpServletResponse servletResponse, Model model) {

		String username = user.getUsername();
		String password = user.getPassword();
		try {
			logger.info("Authenticating User...");
			if (authenticationServiceImpl.isValidUser(username, password)) {
				String sessionID = authenticationServiceImpl.generateCredentialsToUser(username);

				// Set it in the set-cookie header
				Cookie cookie = new Cookie("sessionID", sessionID);
				Cookie userCookie = new Cookie("username", username);
				servletResponse.addCookie(cookie);
				servletResponse.addCookie(userCookie);
			}
			logger.info("Logged in successfully");
			model.addAttribute("transfer", new Transfer());
			return "redirect:/";
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "redirect:/login?status=failed";
	}
}
