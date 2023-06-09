package it.uniroma3.siw.GameHub.repository;

import it.uniroma3.siw.GameHub.model.Game;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends CrudRepository<Game, Long> {

	public boolean existsBySteamcode(Integer steamcode);

	@Query(value= "SELECT game.* FROM users u1 " +
			"JOIN users_owned_games ug1 ON u1.id = ug1.user_id " +
			"JOIN Game game ON ug1.owned_games_id = game.id " +
			"JOIN users_owned_games ug2 ON game.id = ug2.owned_games_id " +
			"JOIN users u2 ON ug2.user_id = u2.id " +
			"WHERE u1.id = :utente1Id AND u2.id = :utente2Id", nativeQuery=true)
	public List<Game> findGiochiInComune(@Param("utente1Id")Long utente1, @Param("utente2Id")Long utente2);

	public Optional<Game> findBySteamcode(Integer steamcode);

}
