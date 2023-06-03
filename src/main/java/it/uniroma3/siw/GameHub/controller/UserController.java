package it.uniroma3.siw.GameHub.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.data.json.recentlyplayedgames.GetRecentlyPlayedGames;
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.GetRecentlyPlayedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

import it.uniroma3.siw.GameHub.SteamAPI;
import it.uniroma3.siw.GameHub.model.Game;
import it.uniroma3.siw.GameHub.model.User;
import it.uniroma3.siw.GameHub.repository.GameRepository;
import it.uniroma3.siw.GameHub.repository.UserRepository;

@Controller
public class UserController {
	
	@Autowired 
	private UserRepository userRepository;

	@Autowired 
	private GameRepository gameRepository;

	@Autowired
	private SteamAPI steamApi;

	@GetMapping("/webUsers")
	public String webUsers(Model model) {
		List<User> lista =(List<User>) userRepository.findAll();
		model.addAttribute("webUsers", lista);
		if(lista.size()==0) {
			model.addAttribute("messaggioErrore", "nessun utente registrato esistente");
		}
		return "webUsers.html";
	}

	@GetMapping("/webUser/{id}")
	public String webUser(@PathVariable("id") Long id,Model model) throws SteamApiException {
		User wu=getWebUserById(id);
		if(wu==null) {
			model.addAttribute("messaggioErrore", "Utente non trovato");
			return "webUser.html"; 
		}
		model.addAttribute("webUser", wu);
		List <Game> lista = top5Games(wu);
		System.out.println(lista);
		System.out.println(lista.size());
		for(Game g: lista) {
			System.out.println(g);
		}
		model.addAttribute("top5Played", lista);
		return "webUser.html";
	}

	@GetMapping("/formNewWebUser")
	public String formNewWebUser(Model model) {
		model.addAttribute("webUser", new User());
		return "formNewWebUser.html";
	}

	@PostMapping("/webUsers")
	public String newWebUser(@ModelAttribute("webUser") User wu, Model model) throws SteamApiException{
		if(!userRepository.existsByEmail(wu.getEmail())) {
			model.addAttribute("webUser", wu);
			userRepository.save(wu);
			return "webUser.html";
		}else {
			model.addAttribute("messaggioErrore", "Utente già esistente");
			return "formNewWebUser.html";
		}
	}

	@GetMapping("/updateOwnedGames/{id}")
	public String RefreshGames(Model model, @PathVariable("id") Long id) throws SteamApiException {
		User wu = this.getWebUserById(id);
		if(wu==null) {
			model.addAttribute("messaggioErrore", "utente non trovato");
			return "webUser.html";
		}
		GetOwnedGamesRequest request =  new GetOwnedGamesRequest.GetOwnedGamesRequestBuilder(wu.getSteamId()).includeAppInfo(true).buildRequest();
		GetOwnedGames gog = steamApi.getClient().<GetOwnedGames>processRequest(request);
		System.out.println("Giochi posseduti: "+gog.getResponse().getGames().size());
		Set<Game> insiemeGiochi = wu.getOwnedGames();
		for(com.lukaspradel.steamapi.data.json.ownedgames.Game apiGame : gog.getResponse().getGames() ) {
			if(!gameRepository.existsBySteamcode(apiGame.getAppid())) {
				Game g = new Game();
				g.setSteamcode(apiGame.getAppid());
				g.setName(apiGame.getName());/**/
				gameRepository.save(g);
				insiemeGiochi.add(g);
			}
		}
		userRepository.save(wu);
		return "redirect:/webUser/"+wu.getId().toString();
	}

	private User getWebUserById(Long id) {
		try {
			return userRepository.findById(id).get();
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private List<Game> top5Games(User user) throws SteamApiException{
		List<Game> topList= new ArrayList<Game>();
		GetRecentlyPlayedGamesRequest request= SteamWebApiRequestFactory.createGetRecentlyPlayedGamesRequest(user.getSteamId(), 5);
		GetRecentlyPlayedGames answer= steamApi.getClient().<GetRecentlyPlayedGames>processRequest(request);
		for(com.lukaspradel.steamapi.data.json.recentlyplayedgames.Game apiGame : answer.getResponse().getGames() ) {
			Game g = new Game();
			g.setSteamcode(apiGame.getAppid());
			g.setName(apiGame.getName());/**/
			gameRepository.save(g);
			topList.add(g);
		}
		return topList;
	}
}
