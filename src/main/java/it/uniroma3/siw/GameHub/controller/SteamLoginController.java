package it.uniroma3.siw.GameHub.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class SteamLoginController {

//	static final private String steamEndpoint = "https://steamcommunity.com/openid/";
//	static final private String steamApiKey = "056BDA5087E6B09FF4E875FD1ACAFD3F";
//	static final private String steamReturnUrl = "http://localhost:8080/login/steam/verify";
	
	
	@GetMapping("/login/steam")
	public String login(Model model) {
		test();
		return "index.html";
	}
	
	private void test() {
	}
}
