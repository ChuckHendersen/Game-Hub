package it.uniroma3.siw.GameHub.repository;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.GameHub.model.WebUser;

public interface WebUserRepository extends CrudRepository<WebUser, Long> {
	public Set<WebUser> findByUsername(String username);
	
	public boolean existsByUsername(String username);
	public boolean existsByEmail(String email);
}
