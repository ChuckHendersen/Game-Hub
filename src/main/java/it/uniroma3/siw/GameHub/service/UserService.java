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
import it.uniroma3.siw.GameHub.exceptions.UserNotFoundException;
import it.uniroma3.siw.GameHub.model.Game;
import it.uniroma3.siw.GameHub.model.User;
import it.uniroma3.siw.GameHub.repository.GameRepository;
import it.uniroma3.siw.GameHub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


	@Transactional
	public Iterable<User> findAll(){
		return this.userRepository.findAll();
	}

	@Transactional
	public User findUserById(Long id) throws UserNotFoundException {
		User user = userRepository.findById(id).orElse(null);
		if(user == null){
			throw new UserNotFoundException("Utente con id: " + id + " non trovato");
		}else{
			return user;
		}

	}

	@Transactional
	public boolean existsById(Long userId){
		return this.userRepository.existsById(userId);
	}

	/**
	 * Metodo che interroga la steam api per ottenere gli ultimi 5 giochi giocati dall'utente di recente
	 * @param id id dell'utente
	 * @return lista di 5 giochi recentemente giocati
	 * @throws SteamApiException se la richiesta alla steam api fallisce
	 * @throws UserNotFoundException se l'utente non è presente nel database
	 */
	@Transactional
	public List<Game> top5Games(Long id) throws SteamApiException, UserNotFoundException {
		User wu = this.findUserById(id);
		List<Game> topList= new ArrayList<>();
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

	@Transactional
	public void updateUserImageFromSteam(Long userId) throws UserNotFoundException, SteamApiException {
		User user = this.findUserById(userId);
		if(user.getSteamId()!=null){
			GetPlayerSummariesRequest request= SteamWebApiRequestFactory.createGetPlayerSummariesRequest(List.of(user.getSteamId()));
			GetPlayerSummaries answer= steamApi.getClient().<GetPlayerSummaries>processRequest(request);
			String imageLink= answer.getResponse().getPlayers().get(0).getAvatarfull();
			user.setSteamProfilePictureLink(imageLink);
		}
	}

	public boolean existsByEmail(String email) {
		return this.userRepository.existsByEmail(email);
	}
}
