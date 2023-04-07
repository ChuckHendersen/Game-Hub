package it.uniroma3.siw.GameHub.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.GameHub.model.Game;

public interface GameRepository extends CrudRepository<Game, Long> {
	
	public boolean existsBySteamcode(Integer steamcode);

}
