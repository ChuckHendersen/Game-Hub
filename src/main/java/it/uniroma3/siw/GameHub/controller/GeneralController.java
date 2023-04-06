package it.uniroma3.siw.GameHub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
<<<<<<< HEAD
import com.lukaspradel.steamapi.core.exception.SteamApiException;
=======
>>>>>>> branch 'main' of https://github.com/ChuckHendersen/ProgettoSIW

@Controller
public class GeneralController {
	
	@GetMapping("/")
	public String index(){
		return "index.html";
	}
}
