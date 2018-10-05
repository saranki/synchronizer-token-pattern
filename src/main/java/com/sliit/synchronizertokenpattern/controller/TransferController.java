/**
 * 
 */
package com.sliit.synchronizertokenpattern.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sliit.synchronizertokenpattern.model.Transfer;
import com.sliit.synchronizertokenpattern.service.impl.AuthenticationServiceImpl;

/**
 * @author Saranki
 *
 */
public class TransferController {
	
	@Autowired
    AuthenticationServiceImpl authenticationService;

    @PostMapping("/transfer")
    public String transferFunds(@ModelAttribute Transfer transfer, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String sessionId = authenticationService.getSessionIdFromCookie(cookies);

        if (authenticationService.isCSRFTokenValid(sessionId, transfer.getCsrf())){
        	
        	System.out.println("Transfer :"+transfer.getCsrf());

            return "redirect:/home?status=success";
        }
        return "redirect:/home?status=failed";
    }
}
