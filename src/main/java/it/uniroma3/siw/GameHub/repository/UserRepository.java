package it.uniroma3.siw.GameHub.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.GameHub.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
	public List<User> findByUsername(String username);
	
	public boolean existsByUsername(String username);
	public boolean existsByUserEmail(String username);
	
	
}
