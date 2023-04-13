package it.uniroma3.siw.GameHub.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.GameHub.Logger.SteamLogin;

@Controller
public class GeneralController {
	
	@Autowired
	SteamLogin externalLogin;
	
	@GetMapping("/")
	public String index(){
		return "index.html";
	}
	
	@GetMapping("/redirection")
	public String redirection() {
		return "redirect:www.google.com";
	}
	
	@GetMapping("/login/steam")
	public String steamLogin(Model model) {
		String result;
		result = "redirect:"+externalLogin.login("http://localhost:8080/login/steam/auth");
		System.out.println(result);
		return result;
	}
	
	@GetMapping("/login/steam/auth")
	public String steamLoginAuth(Model model) { 	
		Map<String,Object> modello = model.asMap();
		System.out.println(modello);
		externalLogin.verify("http://localhost:8080/login/steam/auth", modello);
		return "index.html";
	}
	
	
}
