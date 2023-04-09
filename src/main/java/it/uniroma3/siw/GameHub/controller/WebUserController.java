package it.uniroma3.siw.GameHub.controller;
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
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

import it.uniroma3.siw.GameHub.SteamAPI;
import it.uniroma3.siw.GameHub.model.Game;
import it.uniroma3.siw.GameHub.model.WebUser;
import it.uniroma3.siw.GameHub.repository.GameRepository;
import it.uniroma3.siw.GameHub.repository.WebUserRepository;

@Controller
public class WebUserController {
	@Autowired 
	WebUserRepository webUserRepository;
	
	@Autowired 
	GameRepository gameRepository;
	
	@GetMapping("/webUsers")
	public String webUsers(Model model) {
		List<WebUser> lista =(List<WebUser>) webUserRepository.findAll();
		model.addAttribute("webUsers", lista);
		if(lista.size()==0) {
			model.addAttribute("messaggioErrore", "nessun utente registrato esistente");
		}
		return "webUsers.html";
	}
	
	@GetMapping("/webUser/{id}")
	public String webUser(@PathVariable("id") Long id,Model model) {
		WebUser wu=getWebUserById(id);
		if(wu==null) {
			model.addAttribute("messaggioErrore", "Utente non trovato");
			return "webUser.html";
		}
		model.addAttribute("webUser", wu);
		return "webUser.html";
	}
	
	@GetMapping("/formNewWebUser")
	public String formNewWebUser(Model model) {
		model.addAttribute("webUser", new WebUser());
		return "formNewWebUser.html";
	}
	
	@PostMapping("/webUsers")
	public String newWebUser(@ModelAttribute("webUser") WebUser wu, Model model){
		if(!webUserRepository.existsByEmail(wu.getEmail())) {
			model.addAttribute("webUser", wu);
			webUserRepository.save(wu);
			return "webUser.html"; 
		}else {
			model.addAttribute("messaggioErrore", "Utente già esistente");
			return "formNewWebUser.html";
		}
	}
	
	@GetMapping("/updateOwnedGames/{id}")
	public String RefreshGames(Model model, @PathVariable("id") Long id) throws SteamApiException {
		WebUser wu = this.getWebUserById(id);
		if(wu==null) {
			model.addAttribute("messaggioErrore", "utente non trovato");
			return "webUser.html";
		}
		GetOwnedGamesRequest request =  SteamWebApiRequestFactory.createGetOwnedGamesRequest(wu.getSteamID64());
		GetOwnedGames gog = SteamAPI.client.<GetOwnedGames>processRequest(request);
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
		webUserRepository.save(wu);
		model.addAttribute("webUser", wu);
		return "webUser.html";
	}
	
	private WebUser getWebUserById(Long id) {
		try {
			return webUserRepository.findById(id).get();
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
