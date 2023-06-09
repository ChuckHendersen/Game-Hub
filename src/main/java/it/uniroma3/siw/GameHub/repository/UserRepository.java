package it.uniroma3.siw.GameHub.repository;

import it.uniroma3.siw.GameHub.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface UserRepository extends CrudRepository<User, Long> {
	public Set<User> findByUsername(String username);
	public boolean existsByUsername(String username);
	public boolean existsByEmail(String email);
	public boolean existsBySteamId(String steamId);
	public User getBySteamId(String steamUserID);
		
}
