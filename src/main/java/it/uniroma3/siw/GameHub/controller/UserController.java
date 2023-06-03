package it.uniroma3.siw.GameHub.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.lukaspradel.steamapi.core.exception.SteamApiException;
import it.uniroma3.siw.GameHub.model.Game;
import it.uniroma3.siw.GameHub.model.User;
import it.uniroma3.siw.GameHub.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/webUsers")
	public String webUsers(Model model) {
		List<User> lista = (List<User>) userService.findAll();
		model.addAttribute("webUsers", lista);
		if(lista.size()==0) {
			model.addAttribute("messaggioErrore", "nessun utente registrato esistente");
		}
		return "webUsers.html";
	}

	@GetMapping("/webUser/{id}")
	public String webUser(@PathVariable("id") Long id,Model model) throws SteamApiException {
		User wu=userService.getWebUserById(id);
		if(wu==null) {
			model.addAttribute("messaggioErrore", "Utente non trovato");
			return "webUser.html"; 
		}
		model.addAttribute("webUser", wu);
		List <Game> lista = userService.top5Games(wu);
		model.addAttribute("top5Played", lista);
		return "webUser.html";
	}

	@PostMapping("/webUsers")
	public String newWebUser(@ModelAttribute("webUser") User wu, Model model) throws SteamApiException{
		User user= this.userService.newWebUser(wu);
		if(user==null) {
			model.addAttribute("messaggioErrore", "Utente già esistente");
			return "formNewWebUser.html";
		}
		else {
			model.addAttribute("webUser", wu);
			return "webUser.html";
		}

	}

	@GetMapping("/updateOwnedGames/{id}")
	public String RefreshGames(Model model, @PathVariable("id") Long id) throws SteamApiException {
		User wu = this.userService.refreshGames(id);
		if(wu==null) {
			model.addAttribute("messaggioErrore", "utente non trovato");
			return "webUser.html";
		}
		return "redirect:/webUser/"+wu.getId().toString();
	}




}
