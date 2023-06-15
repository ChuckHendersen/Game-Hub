package it.uniroma3.siw.GameHub.controller;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import it.uniroma3.siw.GameHub.authentication.SteamLogin;
import it.uniroma3.siw.GameHub.controller.form.UserForm;
import it.uniroma3.siw.GameHub.controller.validator.CredentialsValidator;
import it.uniroma3.siw.GameHub.controller.validator.UserValidator;
import it.uniroma3.siw.GameHub.exceptions.InvalidUserOperationException;
import it.uniroma3.siw.GameHub.exceptions.UserNotFoundException;
import it.uniroma3.siw.GameHub.model.Credentials;
import it.uniroma3.siw.GameHub.model.User;
import it.uniroma3.siw.GameHub.service.CredentialsService;
import it.uniroma3.siw.GameHub.service.GameService;
import it.uniroma3.siw.GameHub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class LoggingController {

	@Autowired
	private SteamLogin externalLogin;

	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private CredentialsValidator credentialsValidator;

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private GameService gameService;

	@GetMapping("/login")
	public String login(Model model) {
		return "formLogin.html";
	}
	
	@GetMapping("/success")
    public String defaultAfterLogin(Model model) throws SteamApiException, UserNotFoundException {
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	model.addAttribute("credentials", credentials);
		this.gameService.refreshGames(credentials.getUser().getId()); // NON DOVREBBE MAI SOLLEVARE ECCEZIONI
        return "index.html";
    }
	
	@GetMapping("/register")
	public String formNewWebUser(Model model) {
		model.addAttribute("userForm", new UserForm(this.credentialsService.createCredentials() ,this.userService.createUser()));
		return "formNewWebUser.html";
	}
	
	@PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userForm") UserForm userForm,
    			BindingResult userFormBindingResult,
                Model model) {
        // se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
		Credentials credentials = userForm.getCredentials();
		User user = userForm.getUser();
		this.credentialsValidator.validate(credentials, userFormBindingResult);
		this.userValidator.validate(user, userFormBindingResult);
        if(!userFormBindingResult.hasErrors()) {
            credentials.setUser(user);
            user.setCredentials(credentials);
			user.setUsername(credentials.getUsername());
            credentials=credentialsService.saveCredentials(credentials);
            user = userService.saveUser(user);
            model.addAttribute("user", user);
            return "redirect:/login";
        }
        return "formNewWebUser.html";
    }
	
	@GetMapping("/login/{user_id}/steam")
	public String steamLogin(@PathVariable("user_id") Long userId,Model model) {
		try {
			this.credentialsService.checkCurrentUserIsAuthorized(userId);
			String steamLoginPageURL; // ridireziona al sito di steam per effettuare il login
			steamLoginPageURL = "redirect:"+externalLogin.login("http://localhost:8080/login/"+userId+"/steam/auth");
			return steamLoginPageURL;
		} catch (InvalidUserOperationException e) {
			model.addAttribute("messaggioErrore",e.getMessage());
			return "user.html";
		}
	}

	@GetMapping("/login/{user_id}/steam/auth") // da steam, dopo aver premuto il bottone di login, si ritorna sul nostro sito
	public String steamLoginAuth(@PathVariable("user_id") Long userId, Model model, @RequestParam Map<String,String> allParams) throws SteamApiException { 
		String steamUserID = externalLogin.verify("http://localhost:8080/login/"+userId+"/steam/auth", allParams);
		try {
			this.userService.updateUserSteamId(userId, steamUserID);
			this.gameService.refreshGames(userId);
			return "redirect:/user/"+userId;
		} catch (UserNotFoundException | InvalidUserOperationException e) {
			model.addAttribute("messaggioErrore",e.getMessage());
			return "user.html";
		}
	}
}
