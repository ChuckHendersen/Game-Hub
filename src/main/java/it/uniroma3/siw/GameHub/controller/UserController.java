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

	@GetMapping("/users")
	public String webUsers(Model model) {
		List<User> lista = (List<User>) userService.findAll();
		model.addAttribute("users", lista);
		if(lista.size()==0) {
			model.addAttribute("messaggioErrore", "nessun utente registrato esistente");
		}
		return "users.html";
	}

	@GetMapping("/user/{id}")
	public String webUser(@PathVariable("id") Long id,Model model) throws SteamApiException {
		User wu=userService.findUserById(id);
		if(wu==null) {
			model.addAttribute("messaggioErrore", "Utente non trovato");
		}else {
			model.addAttribute("user", wu);
			Iterable<Game> top5PlayedGames = this.userService.top5Games(id);
			model.addAttribute("top5Played", top5PlayedGames);
		}
		return "user.html";
	}

	@PostMapping("/webUsers")
	public String newWebUser(@ModelAttribute("webUser") User wu, Model model) throws SteamApiException{
		User user= this.userService.newWebUser(wu);
		if(user==null) {
			model.addAttribute("messaggioErrore", "Utente già esistente");
			return "formNewWebUser.html";
		}
		else {
			model.addAttribute("user", wu);
			return "user.html";
		}

	}

	@GetMapping("/updateOwnedGames/{id}")
	public String RefreshGames(Model model, @PathVariable("id") Long id) throws SteamApiException {
		User wu = this.userService.refreshGames(id);
		if(wu==null) {
			model.addAttribute("messaggioErrore", "utente non trovato");
			return "user.html";
		}
		return "redirect:/user/"+wu.getId().toString();
	}

	@GetMapping("/newFollow/{Ua_id}/{Ub_id}")
	public String newFollow(@PathVariable("Ua_id") Long aId, @PathVariable("Ub_id") Long bId, Model model) {
		User b= this.userService.aFollowsB(aId, bId);
		if(b!=null) {
			return "redirect:/user/"+b.getId().toString();
		}
		else {
			return "/error";
		}
	}

	@GetMapping("/deleteFollow/{Ua_id}/{Ub_id}")
	public String deleteFollow(@PathVariable("Ua_id") Long aId, @PathVariable("Ub_id") Long bId, Model model) {
		User b= this.userService.aUnfollowsB(aId, bId);
		if(b!=null) {
			return "redirect:/user/"+b.getId().toString();
		}
		else {
			return "/error";
		}
	}
	
	@GetMapping("/followers/{id}")
	public String followers(@PathVariable("id") Long id, Model model) {
		User user= this.userService.findUserById(id);
		model.addAttribute("user", user);
		return "followers.html";
	}
	
	@GetMapping("/followed/{id}")
	public String followed(@PathVariable("id") Long id, Model model) {
		User user= this.userService.findUserById(id);
		model.addAttribute("user", user);
		return "followed.html";
	}
}
