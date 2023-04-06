package it.uniroma3.siw.GameHub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import it.uniroma3.siw.GameHub.model.WebUser;
import it.uniroma3.siw.GameHub.repository.WebUserRepository;

@Controller
public class WebUserController {
	@Autowired
	WebUserRepository webUserRepository;
	
	@GetMapping("/webUsers")
	public String webUsers(Model model) {
		List<WebUser> lista =(List<WebUser>) webUserRepository.findAll();
		model.addAttribute("webUsers", lista);
		return "webUsers.html";
	}
	
	@GetMapping("/webUser/{id}")
	public String webUser(@PathVariable("id") Long id,Model model) {
		WebUser wu=null;
		try {
			wu=webUserRepository.findById(id).get();
			model.addAttribute("webUser", wu);
		}catch(Exception e) {
			e.printStackTrace(); // utente non trovato
			model.addAttribute("messaggioErrore", "Utente non trovato");
		}
		return "webUser.html";
	}
	
	@GetMapping("/formNewWebUser")
	public String formNewWebUser(Model model) {
		model.addAttribute("webUser", new WebUser());
		return "formNewWebUser.html";
	}
	

	
	@PostMapping("/webUsers")
	public String newWebUser(@ModelAttribute("webUser") WebUser wu, Model model){
		if(!webUserRepository.existsByUserEmail(wu.getUserEmail())) {
			model.addAttribute("webUser", wu);
			webUserRepository.save(wu);
			webUserRepository.save(wu);
			return "webUser.html"; 
		}else {
			model.addAttribute("messaggioErrore", "Utente già esistente");
			return "formNewWebUser.html";
		}
	}
}
