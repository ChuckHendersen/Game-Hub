package it.uniroma3.siw.GameHub.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.GameHub.Logger.SteamLogin;

@Controller
public class LoggingController {
	
	@Autowired
	SteamLogin externalLogin;
	
	@GetMapping("/login/steam")
	public String steamLogin(Model model) {
		String steamLogginPageURL; // ridireziona al sito di steam per effettuare il login
		steamLogginPageURL = "redirect:"+externalLogin.login("http://localhost:8080/login/steam/auth");
		return steamLogginPageURL;
	}
	
	@GetMapping("/login/steam/auth") // da steam, dopo aver premuto il bottone di login, si ritorna sul nostro sito
	public String steamLoginAuth(Model model, @RequestParam Map<String,String> allParams) { 
		//System.out.println(allParams);
		String steamUserID = externalLogin.verify("http://localhost:8080/login/steam/auth", allParams);
		
		model.addAttribute("loggato", steamUserID);
		return "index.html";
	}
}
