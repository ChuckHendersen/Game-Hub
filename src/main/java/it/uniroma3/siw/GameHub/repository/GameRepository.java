package it.uniroma3.siw.GameHub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.GameHub.model.Game;
import it.uniroma3.siw.GameHub.model.User;

public interface GameRepository extends CrudRepository<Game, Long> {

	public boolean existsBySteamcode(Integer steamcode);

	@Query(value= "SELECT gioco.* FROM Utente u1 " +
			"JOIN utente_giochi ug1 ON u1.id = ug1.utente_id " +
			"JOIN Gioco gioco ON ug1.giochiPosseduti_id = gioco.id " +
			"JOIN utente_giochi ug2 ON gioco.id = ug2.giochiPosseduti_id " +
			"JOIN Utente u2 ON ug2.utente_id = u2.id " +
			"WHERE u1.id = :utente1Id AND u2.id = :utente2Id", nativeQuery=true)
	public List<Game> findGiochiInComune(@Param("utente1Id")User utente1, @Param("utente2Id")User utente2);

}
