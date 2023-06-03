package it.uniroma3.siw.GameHub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.GameHub.model.Credentials;
import it.uniroma3.siw.GameHub.service.CredentialsService;


@ControllerAdvice
public class GlobalController {
	
	@Autowired
	private CredentialsService credentialsService;
	
    @ModelAttribute("userDetails")
    public UserDetails getUser() {
        UserDetails user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return user;
    }
    
    @ModelAttribute("credentials")
    public Credentials getCredentials() {
    	Credentials credentials = null;
    	UserDetails user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            credentials = this.credentialsService.getCredentials(user.getUsername());
        }
        return credentials;
    }
    @GetMapping("/risorsaNonTrovata")
    	public String risorsaNonTrovata() {
    		return "risorsaNonTrovata.html";
    	}
    
}