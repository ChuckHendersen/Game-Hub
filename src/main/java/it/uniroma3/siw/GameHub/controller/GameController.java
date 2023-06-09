package it.uniroma3.siw.GameHub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import it.uniroma3.siw.GameHub.model.Game;
import it.uniroma3.siw.GameHub.service.GameService;

@Controller
public class GameController {
	
		
	@Autowired
	private GameService gameService;
	
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
		Iterable<Game> insiemeGiochi = this.gameService.findGiochiInComune(id1, id2);
		if(insiemeGiochi==null) {
			model.addAttribute("messaggioErrore", "nessun gioco in comune");
		}else {
			model.addAttribute("games", insiemeGiochi);
		}
		return "compareGames.html";
	}
}
