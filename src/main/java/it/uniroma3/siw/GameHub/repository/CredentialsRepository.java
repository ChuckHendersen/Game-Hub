package it.uniroma3.siw.GameHub.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.GameHub.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

}