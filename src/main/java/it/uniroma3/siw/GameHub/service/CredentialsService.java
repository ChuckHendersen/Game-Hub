package it.uniroma3.siw.GameHub.service;

import it.uniroma3.siw.GameHub.exceptions.InvalidUserOperationException;
import it.uniroma3.siw.GameHub.model.Credentials;
import it.uniroma3.siw.GameHub.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CredentialsService {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected CredentialsRepository credentialsRepository;

    @Transactional
    public Credentials getCredentials(Long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Credentials getCredentials(String userName) {
        Optional<Credentials> result = this.credentialsRepository.findByUsername(userName);
        return result.orElse(null);
    }
    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }

	public Credentials createCredentials() {
		return new Credentials();
	}

    public boolean existsByUsername(String username) {
        return this.credentialsRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public Credentials getCurrentCredentials() {
        Credentials credentials = null;
        UserDetails user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            credentials = this.getCredentials(user.getUsername());
        }
        return credentials;
    }

    @Transactional
    public void checkCurrentUserIsAuthorized(Long userId) throws InvalidUserOperationException {
        if(!this.getCurrentCredentials().getUser().getId().equals(userId)){
            throw new InvalidUserOperationException("Utente non autorizzato");
        }
    }
}
