package it.uniroma3.siw.GameHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.GameHub.model.Game;
import it.uniroma3.siw.GameHub.repository.GameRepository;
import jakarta.transaction.Transactional;

@Service
public class GameService {

	@Autowired
	private GameRepository gameRepository;
	
	
	@Transactional
	public Iterable<Game> findAll(){
		return this.gameRepository.findAll();
	}

	@Transactional
	public Game findById(Long id) {
		return this.gameRepository.findById(id).orElse(null);
	}

	public Iterable<Game> findGiochiInComune(Long utente1Id, Long utente2Id) {
		return this.gameRepository.findGiochiInComune(utente1Id, utente2Id);
	}
}
