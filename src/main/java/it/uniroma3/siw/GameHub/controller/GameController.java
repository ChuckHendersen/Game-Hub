package it.uniroma3.siw.GameHub.controller;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import it.uniroma3.siw.GameHub.exceptions.SameUserException;
import it.uniroma3.siw.GameHub.exceptions.UserNotFoundException;
import it.uniroma3.siw.GameHub.model.Game;
import it.uniroma3.siw.GameHub.model.User;
import it.uniroma3.siw.GameHub.service.GameService;
import it.uniroma3.siw.GameHub.service.UserService;
import org.aspectj.bridge.IMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class GameController {
	
		
	@Autowired
	private GameService gameService;

	@Autowired
	private UserService userService;
	
	@GetMapping("/games")
	public String games(Model model) {
		Iterable<Game> insiemeGiochi = getGames();
		if(insiemeGiochi==null) {
			model.addAttribute("messaggioErrore", "nessun gioco esiste nel database");
		}else {
			model.addAttribute("games", insiemeGiochi);	
		}
		return "games.html";
	}
	
	@GetMapping("/game/{id}")
	public String game(@PathVariable("id") Long id, Model model) {
		Game game = getGame(id);
		if(game==null) {
			model.addAttribute("messaggioErrore", "Il gioco non esiste");
		}else {
			model.addAttribute("game", game);
		}
		return "game.html";
	}

	@GetMapping("/updateOwnedGames/{id}")
	public String RefreshGames(Model model, @PathVariable("id") Long id) throws SteamApiException {
		User wu = null;
		try {
			wu = this.userService.refreshGames(id);
			model.addAttribute("user", wu);
			return "redirect:/user/" + wu.getId().toString();
		} catch (UserNotFoundException e) {
			model.addAttribute("messaggioErrore", e.getMessage());
			return "user.html";
		}
	}

	private Iterable<Game> getGames(){
		try {
			return gameService.findAll();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Game getGame(Long id) {
		try {
			return gameService.findById(id);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@GetMapping("/compareGames/{id1}/{id2}")
	public String compareGames(@PathVariable("id1") Long id1, @PathVariable("id2") Long id2, Model model) {
		List<Game> insiemeGiochi;
		try {
			model.addAttribute("userA", this.userService.findUserById(id1));
			model.addAttribute("userB", this.userService.findUserById(id2));
			insiemeGiochi =(List<Game>) this.gameService.findGiochiInComune(id1, id2);
			if(insiemeGiochi==null || insiemeGiochi.size()==0) {
				model.addAttribute("messaggioErrore", "nessun gioco in comune");
			}else {
				model.addAttribute("games", insiemeGiochi);
			}
		} catch (SameUserException | UserNotFoundException e) {
			model.addAttribute("messaggioErrore", e.getMessage());
		}
		return "compareGames.html";
	}
}
