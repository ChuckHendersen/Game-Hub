package it.uniroma3.siw.GameHub.service;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.data.json.recentlyplayedgames.GetRecentlyPlayedGames;
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.GetRecentlyPlayedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;
import it.uniroma3.siw.GameHub.SteamAPI;
import it.uniroma3.siw.GameHub.exceptions.SameUserException;
import it.uniroma3.siw.GameHub.exceptions.UserNotFoundException;
import it.uniroma3.siw.GameHub.model.Game;
import it.uniroma3.siw.GameHub.model.User;
import it.uniroma3.siw.GameHub.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GameService {

	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private UserService userService;

	@Autowired
	private SteamAPI steamApi;

	@Transactional
	public Iterable<Game> findAll(){
		return this.gameRepository.findAll();
	}

	@Transactional
	public Game findById(Long id) {
		return this.gameRepository.findById(id).orElse(null);
	}


	@Transactional
	public Iterable<Game> findGiochiInComune(Long utente1Id, Long utente2Id) throws SameUserException, UserNotFoundException {
		if(!this.userService.existsById(utente1Id) || !this.userService.existsById(utente2Id)){
			throw new UserNotFoundException("Uno o più utenti da comparare non esistono");
		}
		if(utente1Id.equals(utente2Id)){
			throw new SameUserException("E' inutile cercare giochi in comune con te stesso");
		}
		return this.gameRepository.findGiochiInComune(utente1Id, utente2Id);
	}

    public Iterable<Game> getOwnedGames(Long userId) throws UserNotFoundException {
		User user = this.userService.findUserById(userId);
		return user.getOwnedGames();
    }

	/**
	 * Metodo che interroga la steam api per ottenere gli ultimi 5 giochi giocati dall'utente di recente
	 *
	 * @param id id dell'utente
	 * @return lista di 5 giochi recentemente giocati
	 * @throws SteamApiException     se la richiesta alla steam api fallisce
	 * @throws UserNotFoundException se l'utente non è presente nel database
	 */
	@Transactional
	public List<Game> top5Games(Long id) throws SteamApiException, UserNotFoundException {
		User wu = this.userService.findUserById(id);
		List<Game> topList = new ArrayList<>();
		if (wu.getSteamId() != null && !wu.getSteamId().equals("")) {
			GetRecentlyPlayedGamesRequest request = SteamWebApiRequestFactory.createGetRecentlyPlayedGamesRequest(wu.getSteamId(), 5);
			GetRecentlyPlayedGames answer = steamApi.getClient().<GetRecentlyPlayedGames>processRequest(request);
			for (com.lukaspradel.steamapi.data.json.recentlyplayedgames.Game apiGame : answer.getResponse().getGames()) {
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
		User wu = this.userService.findUserById(id);
		if (wu != null && wu.getSteamId() != null && !wu.getSteamId().equals("")) {
			GetOwnedGamesRequest request = new GetOwnedGamesRequest.GetOwnedGamesRequestBuilder(wu.getSteamId()).includeAppInfo(true).buildRequest();
			GetOwnedGames gog = steamApi.getClient().<GetOwnedGames>processRequest(request);
			System.out.println("Giochi posseduti: " + gog.getResponse().getGames().size());
			Set<Game> insiemeGiochi = wu.getOwnedGames();
			for (com.lukaspradel.steamapi.data.json.ownedgames.Game apiGame : gog.getResponse().getGames()) {
				if (!gameRepository.existsBySteamcode(apiGame.getAppid())) {
					Game g = new Game();
					g.setSteamcode(apiGame.getAppid());
					g.setName(apiGame.getName());
					insiemeGiochi.add(g);
					gameRepository.save(g);
				} else {
					Game g = gameRepository.findBySteamcode(apiGame.getAppid()).orElse(null);
					insiemeGiochi.add(g);
				}
			}
			wu = userService.save(wu);
		}
		return wu;
	}
}
