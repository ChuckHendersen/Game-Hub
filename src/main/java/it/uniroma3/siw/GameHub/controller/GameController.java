package it.uniroma3.siw.GameHub.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.GameHub.model.Game;
import it.uniroma3.siw.GameHub.repository.GameRepository;

@Controller
public class GameController {
	
	@Autowired
	public GameRepository gameRepository;
	
	@GetMapping("/games")
	public String games(Model model) {
		List<Game> insiemeGiochi = getGames();
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
	
	private List<Game> getGames(){
		try {
			return (List<Game>) gameRepository.findAll();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Game getGame(Long id) {
		try {
			return gameRepository.findById(id).get();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
