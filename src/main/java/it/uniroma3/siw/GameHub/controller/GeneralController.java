package it.uniroma3.siw.GameHub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.lukaspradel.steamapi.core.exception.SteamApiException;

@Controller
public class GeneralController {
	
	@GetMapping("/")
	public String index() throws SteamApiException {
		return "index.html";
	}
}
