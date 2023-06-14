package it.uniroma3.siw.GameHub.service;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.data.json.playersummaries.GetPlayerSummaries;
import com.lukaspradel.steamapi.data.json.recentlyplayedgames.GetRecentlyPlayedGames;
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.GetPlayerSummariesRequest;
import com.lukaspradel.steamapi.webapi.request.GetRecentlyPlayedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;
import it.uniroma3.siw.GameHub.SteamAPI;
import it.uniroma3.siw.GameHub.exceptions.InvalidUserOperationException;
import it.uniroma3.siw.GameHub.exceptions.UserNotFoundException;
import it.uniroma3.siw.GameHub.model.Game;
import it.uniroma3.siw.GameHub.model.Picture;
import it.uniroma3.siw.GameHub.model.User;
import it.uniroma3.siw.GameHub.repository.GameRepository;
import it.uniroma3.siw.GameHub.repository.PictureRepository;
import it.uniroma3.siw.GameHub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SteamAPI steamApi;

    @Autowired
    private CredentialsService credentialsService;

    @Transactional
    public Iterable<User> findAll() {
        return this.userRepository.findAll();
    }

    @Transactional
    public User findUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("Utente con id: " + id + " non trovato");
        } else {
            return user;
        }

    }

    @Transactional
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Transactional
    public boolean existsById(Long userId) {
        return this.userRepository.existsById(userId);
    }

    public User createUser() {
        return new User();
    }

    @Transactional
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Transactional
    public void updateUserSteamId(Long userId, String steamUserID) throws UserNotFoundException, InvalidUserOperationException {
        this.credentialsService.checkCurrentUserIsAuthorized(userId);
        User user = this.findUserById(userId);
        if (user != null) {
            user.setSteamId(steamUserID);
            this.userRepository.save(user);
        }
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public List<User> getUsersContainingUsername(String username) {
    	return (List<User>) this.userRepository.findByUsernameContaining(username);
    }
}
