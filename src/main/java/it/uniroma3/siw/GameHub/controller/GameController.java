package it.uniroma3.siw.GameHub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

import it.uniroma3.siw.GameHub.SteamAPI;
import it.uniroma3.siw.GameHub.model.WebUser;
import it.uniroma3.siw.GameHub.repository.GameRepository;

@Controller
public class GameController {
	
	@Autowired
	GameRepository gameRepository;
	
	static public void RefreshGames(WebUser wu) throws SteamApiException {
		GetOwnedGamesRequest request =  SteamWebApiRequestFactory.createGetOwnedGamesRequest(wu.getSteamID64());
		GetOwnedGames gog = SteamAPI.client.<GetOwnedGames>processRequest(request);
		System.out.println("Giochi posseduti: "+gog.getResponse().getGames().size());
	}
	
}
