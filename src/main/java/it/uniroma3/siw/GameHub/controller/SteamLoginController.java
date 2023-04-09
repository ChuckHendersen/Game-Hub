package it.uniroma3.siw.GameHub.controller;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SteamLoginController {
	
	@GetMapping("/login/steam")
	public void login(Model model) {
		
	}
}
