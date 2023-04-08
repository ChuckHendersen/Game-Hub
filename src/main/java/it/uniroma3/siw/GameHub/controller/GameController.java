package it.uniroma3.siw.GameHub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import it.uniroma3.siw.GameHub.repository.GameRepository;

@Controller
public class GameController {
	
	@Autowired
	public GameRepository gameRepository;
	
	public String games(Model model) {
		return "games.html";
	}
	
}
