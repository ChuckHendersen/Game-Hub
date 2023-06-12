package it.uniroma3.siw.GameHub.controller;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import it.uniroma3.siw.GameHub.exceptions.UserNotFoundException;
import it.uniroma3.siw.GameHub.model.Credentials;
import it.uniroma3.siw.GameHub.model.Game;
import it.uniroma3.siw.GameHub.model.User;
import it.uniroma3.siw.GameHub.service.CredentialsService;
import it.uniroma3.siw.GameHub.service.FollowService;
import it.uniroma3.siw.GameHub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FollowService followService;
    @Autowired
    private CredentialsService credentialsService;


    @GetMapping("/users")
    public String webUsers(Model model) {
        List<User> lista = (List<User>) userService.findAll();
        model.addAttribute("users", lista);
        if (lista.size() == 0) {
            model.addAttribute("messaggioErrore", "nessun utente registrato esistente");
        }
        return "users.html";
    }

    @GetMapping("/user/{id}")
    public String webUser(@PathVariable("id") Long id, Model model) throws SteamApiException {
        try {
            User wu = userService.findUserById(id);
            model.addAttribute("user", wu);
            Iterable<Game> top5PlayedGames = this.userService.top5Games(id);
            model.addAttribute("top5Played", top5PlayedGames);
            model.addAttribute("giaSeguito", this.followService.aFollowsBBool(this.getCredentials().getUser().getId(), id));
        } catch (UserNotFoundException e) {
            model.addAttribute("messaggioErrore", e.getMessage());
        }
        return "user.html";
    }

    /*@PostMapping("/webUsers")
    public String newWebUser(@ModelAttribute("webUser") User wu, Model model) throws SteamApiException {
        User user = this.userService.newWebUser(wu);
        if (user == null) {
            model.addAttribute("messaggioErrore", "Utente già esistente");
            return "formNewWebUser.html";
        } else {
            model.addAttribute("user", wu);
            return "user.html";
        }
    }*/

    @GetMapping("/followers/{id}")
    public String followers(@PathVariable("id") Long id, Model model) {
        User user = null;
        try {
            user = this.userService.findUserById(id);
            model.addAttribute("user", user);
            return "followers.html";
        } catch (UserNotFoundException e) {
            model.addAttribute("messaggioErrore", e.getMessage());
            return "user.html";
        }
    }

    @GetMapping("/followed/{id}")
    public String followed(@PathVariable("id") Long id, Model model) {
        User user = null;
        try {
            user = this.userService.findUserById(id);
            model.addAttribute("user", user);
            return "followed.html";
        } catch (UserNotFoundException e) {
            model.addAttribute("messaggioErrore", e.getMessage());
            return "user.html";
        }
    }

    private Credentials getCredentials() {
        Credentials credentials = null;
        UserDetails user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            credentials = this.credentialsService.getCredentials(user.getUsername());
        }
        return credentials;
    }

    @GetMapping("/editUserPicture/{id}")
    public String editUserPicture(@PathVariable("id") Long id, Model model) {
        User user = null;
        try {
            user = this.userService.findUserById(id);
            model.addAttribute("user", user);
            return "editUserPicture.html";
        } catch (UserNotFoundException e) {
            model.addAttribute("messaggioErrore", e.getMessage());
            return "user.html";
        }
    }


    @GetMapping("/setProfileImageFromLink/{userId}")
    public String setProfileImageFromLink(@PathVariable("userId") Long userId, Model model){
        try {
            this.userService.updateUserImageFromSteam(userId);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SteamApiException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/user/"+userId;
    }

    @GetMapping("/setProfileImageFromFile/{userId}")
    public String setProfileImageFromFile(@PathVariable("userId") Long userId, MultipartFile file, Model model){
        try {
            this.userService.updateUserImageFromFile(userId, file);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/user/"+userId;
    }
}
