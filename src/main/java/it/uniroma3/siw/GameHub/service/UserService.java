package it.uniroma3.siw.GameHub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
	public User getWebUserById(Long id) {
		return userRepository.findById(id).orElse(null);

	}

	@Transactional
	public List<Game> top5Games(Long id) throws SteamApiException{
		User wu = this.getWebUserById(id);
		List<Game> topList= new ArrayList<Game>();
		if(wu.getSteamId() != null && !wu.getSteamId().equals("")) {
			GetRecentlyPlayedGamesRequest request= SteamWebApiRequestFactory.createGetRecentlyPlayedGamesRequest(wu.getSteamId(), 5);
			GetRecentlyPlayedGames answer= steamApi.getClient().<GetRecentlyPlayedGames>processRequest(request);
			for(com.lukaspradel.steamapi.data.json.recentlyplayedgames.Game apiGame : answer.getResponse().getGames() ) {
				Game g = new Game();
				g.setSteamcode(apiGame.getAppid());
				g.setName(apiGame.getName());/**/
				gameRepository.save(g);
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
	public User refreshGames(Long id) throws SteamApiException {
		User wu= this.getWebUserById(id);
		if(wu!=null) {
			GetOwnedGamesRequest request =  new GetOwnedGamesRequest.GetOwnedGamesRequestBuilder(wu.getSteamId()).includeAppInfo(true).buildRequest();
			GetOwnedGames gog = steamApi.getClient().<GetOwnedGames>processRequest(request);
			System.out.println("Giochi posseduti: "+gog.getResponse().getGames().size());
			Set<Game> insiemeGiochi = wu.getOwnedGames();
			for(com.lukaspradel.steamapi.data.json.ownedgames.Game apiGame : gog.getResponse().getGames() ) {
				if(!gameRepository.existsBySteamcode(apiGame.getAppid())) {
					Game g = new Game();
					g.setSteamcode(apiGame.getAppid());
					g.setName(apiGame.getName());/**/
					gameRepository.save(g);
					insiemeGiochi.add(g);
				}
			}
			wu= userRepository.save(wu);
		}
		return wu;
	}

	/**
	 * Metodo che consente allo user A di seguire user B
	 * @param aId Id dello user che vuole seguire B
	 * @param bId Id dello user che viene seguito da A
	 * @return user A se l'operazione ha successo, null altrimenti
	 */
	@Transactional
	public User aFollowsB(Long aId, Long bId) {
		User a = this.getWebUserById(aId);
		User b = this.getWebUserById(bId);
		if(a!= null && b!= null) {
			b.addFollower(a);
			a.addFollowed(b);
			this.userRepository.save(b);
			return this.userRepository.save(a);
		}else {
			return null;
		}
	}

	public User createUser() {
		// TODO Auto-generated method stub
		return new User();
	}
	@Transactional
	public User saveUser(User user) {
		return this.userRepository.save(user);
	}

	public void updateUserSteamId(Long userId, String steamUserID) {
		User user = this.getWebUserById(userId);
		if(user!=null) {
			user.setSteamId(steamUserID);
			this.userRepository.save(user);
		}
	}
}
