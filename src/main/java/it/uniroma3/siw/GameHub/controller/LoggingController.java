package it.uniroma3.siw.GameHub.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.lukaspradel.steamapi.core.exception.SteamApiException;
import it.uniroma3.siw.GameHub.authentication.SteamLogin;
import it.uniroma3.siw.GameHub.model.Credentials;
import it.uniroma3.siw.GameHub.model.User;
import it.uniroma3.siw.GameHub.service.CredentialsService;
import it.uniroma3.siw.GameHub.service.UserService;
import jakarta.validation.Valid;

@Controller
public class LoggingController {

	@Autowired
	private SteamLogin externalLogin;

	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String login(Model model) {
		return "formLogin.html";
	}
	
	@GetMapping("/success")
    public String defaultAfterLogin(Model model) {
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	model.addAttribute("credentials", credentials);
        return "index.html";
    }
	
	@GetMapping("/register")
	public String formNewWebUser(Model model) {
		model.addAttribute("user", this.userService.createUser());
		model.addAttribute("newCredentials", this.credentialsService.createCredentials());
		return "formNewWebUser.html";
	}
	
	@PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, 
    			BindingResult userBindingResult, 
    			@Valid @ModelAttribute("newCredentials") Credentials credentials,
                BindingResult credentialsBindingResult,
                Model model) {
        // se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
            credentials.setUser(user);
            user.setCredentials(credentials);
            credentials=credentialsService.saveCredentials(credentials);
            user = userService.saveUser(user);
            model.addAttribute("user", user);
            return "redirect:/login";
        }
        System.out.println(userBindingResult.getNestedPath());
        System.out.println(credentialsBindingResult.getNestedPath());
        return "redirect:/";
    }
	
	@GetMapping("/login/{user_id}/steam")
	public String steamLogin(@PathVariable("user_id") Long userId,Model model) {
		String steamLogginPageURL; // ridireziona al sito di steam per effettuare il login
		steamLogginPageURL = "redirect:"+externalLogin.login("http://localhost:8080/login/"+userId+"/steam/auth");
		return steamLogginPageURL;
	}

//	@GetMapping("/login/{user_id}/steam/auth") // da steam, dopo aver premuto il bottone di login, si ritorna sul nostro sito
//	public String steamLoginAuth(@PathVariable("user_id") Long userId, Model model, @RequestParam Map<String,String> allParams) throws SteamApiException { 
//		User current= this.userService.getWebUserById(userId);
//		String steamUserID = externalLogin.verify("http://localhost:8080/login/steam/auth", allParams);
//		if(userRepository.existsBySteamId(steamUserID)) {
//			current = userRepository.getBySteamId(steamUserID);
//		} else {
//			current= new User();
//			current.setSteamId(steamUserID);
//			GetPlayerSummariesRequest request= SteamWebApiRequestFactory.createGetPlayerSummariesRequest(Arrays.asList(steamUserID));
//			GetPlayerSummaries answer = steamApi.getClient().<GetPlayerSummaries>processRequest(request);
//			current.setUsername(answer.getResponse().getPlayers().get(0).getPersonaname());
//			userRepository.save(current);
//
//		}
//		//model.addAttribute("webUser", current);
//		return "redirect:"+"/updateOwnedGames/"+current.getId().toString();
//
//	}
	@GetMapping("/login/{user_id}/steam/auth") // da steam, dopo aver premuto il bottone di login, si ritorna sul nostro sito
	public String steamLoginAuth(@PathVariable("user_id") Long userId, Model model, @RequestParam Map<String,String> allParams) throws SteamApiException { 
		String steamUserID = externalLogin.verify("http://localhost:8080/login/"+userId+"/steam/auth", allParams);
		this.userService.updateUserSteamId(userId, steamUserID);
		return "redirect:/webUser/"+userId;
	}


}
