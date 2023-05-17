package it.uniroma3.siw.GameHub.controller;

import java.util.Arrays;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.playersummaries.GetPlayerSummaries;
import com.lukaspradel.steamapi.webapi.request.GetPlayerSummariesRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

import it.uniroma3.siw.GameHub.SteamAPI;
import it.uniroma3.siw.GameHub.Logger.SteamLogin;
import it.uniroma3.siw.GameHub.model.User;
import it.uniroma3.siw.GameHub.repository.UserRepository;

@Controller
public class LoggingController {

	@Autowired
	SteamLogin externalLogin;

	@Autowired
	SteamAPI steamApi;

	@Autowired
	UserRepository userRepository;

	@GetMapping("/login/steam")
	public String steamLogin(Model model) {
		String steamLogginPageURL; // ridireziona al sito di steam per effettuare il login
		steamLogginPageURL = "redirect:"+externalLogin.login("http://localhost:8080/login/steam/auth");
		return steamLogginPageURL;
	}

	@GetMapping("/login/steam/auth") // da steam, dopo aver premuto il bottone di login, si ritorna sul nostro sito
	public String steamLoginAuth(Model model, @RequestParam Map<String,String> allParams) throws SteamApiException { 
		User current;
		String steamUserID = externalLogin.verify("http://localhost:8080/login/steam/auth", allParams);
		if(userRepository.existsBySteamId(steamUserID)) {
			current = userRepository.getBySteamId(steamUserID);
		} else {
			current= new User();
			current.setSteamId(steamUserID);
			GetPlayerSummariesRequest request= SteamWebApiRequestFactory.createGetPlayerSummariesRequest(Arrays.asList(steamUserID));
			GetPlayerSummaries answer = steamApi.getClient().<GetPlayerSummaries>processRequest(request);
			current.setUsername(answer.getResponse().getPlayers().get(0).getPersonaname());
			userRepository.save(current);
			
		}
		//model.addAttribute("webUser", current);
		return "redirect:"+"/updateOwnedGames/"+current.getId().toString();

	}


}
