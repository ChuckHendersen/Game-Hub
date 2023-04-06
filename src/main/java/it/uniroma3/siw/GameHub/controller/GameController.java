package it.uniroma3.siw.GameHub.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

import it.uniroma3.siw.GameHub.SteamAPI;
import it.uniroma3.siw.GameHub.model.Game;
import it.uniroma3.siw.GameHub.model.WebUser;
import it.uniroma3.siw.GameHub.repository.GameRepository;
import it.uniroma3.siw.GameHub.repository.WebUserRepository;

@Controller
public class GameController {
	
	@Autowired
	public GameRepository gameRepository;
	
	static public void RefreshGames(WebUser wu, WebUserRepository wur, GameRepository gr) throws SteamApiException {
		GetOwnedGamesRequest request =  SteamWebApiRequestFactory.createGetOwnedGamesRequest(wu.getSteamID64());
		GetOwnedGames gog = SteamAPI.client.<GetOwnedGames>processRequest(request);
		System.out.println("Giochi posseduti: "+gog.getResponse().getGames().size());
		Set<Game> listaGiochi = new HashSet<>();
		for(com.lukaspradel.steamapi.data.json.ownedgames.Game apiGame : gog.getResponse().getGames() ) {
			Game g = new Game();
//			System.out.println(apiGame.getName());
//			System.out.println(apiGame.getAppid());
			g.setSteamcode(apiGame.getAppid());
			g.setGamename(apiGame.getName());
			gr.save(g);
			listaGiochi.add(g);
		}
		wu.setOwnedGames(listaGiochi);
		System.out.println(listaGiochi);
		wur.save(wu);
	}
	
}
