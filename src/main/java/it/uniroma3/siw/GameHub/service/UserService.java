package it.uniroma3.siw.GameHub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import it.uniroma3.siw.GameHub.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.data.json.recentlyplayedgames.GetRecentlyPlayedGames;
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.GetRecentlyPlayedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

import it.uniroma3.siw.GameHub.SteamAPI;
import it.uniroma3.siw.GameHub.model.Game;
import it.uniroma3.siw.GameHub.model.User;
import it.uniroma3.siw.GameHub.repository.GameRepository;
import it.uniroma3.siw.GameHub.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired 
	private UserRepository userRepository;

	@Autowired 
	private GameRepository gameRepository;

	@Autowired
	private SteamAPI steamApi;


	@Transactional
	public Iterable<User> findAll(){
		return this.userRepository.findAll();
	}

	@Transactional
	public User findUserById(Long id) throws UserNotFoundException {
		User user = userRepository.findById(id).orElse(null);
		if(user == null){
			throw new UserNotFoundException("User with id: " + id + " not found");
		}else{
			return user;
		}

	}

	@Transactional
	public List<Game> top5Games(Long id) throws SteamApiException, UserNotFoundException {
		User wu = this.findUserById(id);
		List<Game> topList= new ArrayList<Game>();
		if(wu.getSteamId() != null && !wu.getSteamId().equals("")) {
			GetRecentlyPlayedGamesRequest request= SteamWebApiRequestFactory.createGetRecentlyPlayedGamesRequest(wu.getSteamId(), 5);
			GetRecentlyPlayedGames answer= steamApi.getClient().<GetRecentlyPlayedGames>processRequest(request);
			for(com.lukaspradel.steamapi.data.json.recentlyplayedgames.Game apiGame : answer.getResponse().getGames() ) {
				Game g = new Game();
				g.setSteamcode(apiGame.getAppid());
				g.setName(apiGame.getName());
				topList.add(g);
			}
		}
		return topList;
	}

	@Transactional
	public User newWebUser(User wu) {
		User user=null;
		if(!userRepository.existsByEmail(wu.getEmail())) {
			user=userRepository.save(wu);
		} 
		return user;
	}
	@Transactional
	public User refreshGames(Long id) throws SteamApiException, UserNotFoundException {
		User wu= this.findUserById(id);
		if(wu!=null && wu.getSteamId()!=null && !wu.getSteamId().equals("")){
			GetOwnedGamesRequest request =  new GetOwnedGamesRequest.GetOwnedGamesRequestBuilder(wu.getSteamId()).includeAppInfo(true).buildRequest();
			GetOwnedGames gog = steamApi.getClient().<GetOwnedGames>processRequest(request);
			System.out.println("Giochi posseduti: "+gog.getResponse().getGames().size());
			Set<Game> insiemeGiochi = wu.getOwnedGames();
			for(com.lukaspradel.steamapi.data.json.ownedgames.Game apiGame : gog.getResponse().getGames() ) {
				if(!gameRepository.existsBySteamcode(apiGame.getAppid())) {
					Game g = new Game();
					g.setSteamcode(apiGame.getAppid());
					g.setName(apiGame.getName());
					insiemeGiochi.add(g);
					gameRepository.save(g);
				}else{
					Game g = gameRepository.findBySteamcode(apiGame.getAppid()).orElse(null);
					insiemeGiochi.add(g);
				}
			}
			wu= userRepository.save(wu);
		}
		return wu;
	}

	public User createUser() {
		return new User();
	}

	@Transactional
	public User saveUser(User user) {
		return this.userRepository.save(user);
	}
	@Transactional
	public void updateUserSteamId(Long userId, String steamUserID) throws UserNotFoundException {
		User user = this.findUserById(userId);
		if(user!=null) {
			user.setSteamId(steamUserID);
			this.userRepository.save(user);
		}
	}

	public boolean existsByEmail(String email) {
		return this.userRepository.existsByEmail(email);
	}
}
