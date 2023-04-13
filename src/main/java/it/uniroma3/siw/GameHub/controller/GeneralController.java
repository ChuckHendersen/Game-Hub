package it.uniroma3.siw.GameHub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {
	
	@GetMapping("/")
	public String index(){
		return "index.html";
	}
	
	//fatto per testing
	@GetMapping("/redirect")
	public String redirection() {
		return "redirect:http://google.com";
	}
}
