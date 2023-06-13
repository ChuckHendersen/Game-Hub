package it.uniroma3.siw.GameHub.service;

import it.uniroma3.siw.GameHub.exceptions.SameUserException;
import it.uniroma3.siw.GameHub.exceptions.UserNotFoundException;
import it.uniroma3.siw.GameHub.model.Game;
import it.uniroma3.siw.GameHub.model.User;
import it.uniroma3.siw.GameHub.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private UserService userService;

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
}
