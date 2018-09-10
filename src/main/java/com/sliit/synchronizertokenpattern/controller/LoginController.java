package com.sliit.synchronizertokenpattern.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
	@GetMapping("/")
	public String goToLogin(){
		return "login";
	}
}
