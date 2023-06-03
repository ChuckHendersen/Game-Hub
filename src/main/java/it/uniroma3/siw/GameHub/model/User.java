package it.uniroma3.siw.GameHub.model;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String username;
	private String email;
	private String steamId;
	
	@OneToOne(mappedBy = "user")
	private Credentials credentials;
	
	@OneToMany
	private Set<User> followed;
	
	@OneToMany
	private Set<User> followers;
	
	@ManyToMany
	private Set<Game> ownedGames;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String userEmail) {
		this.email = userEmail;
	}
	public String getSteamId() {
		return steamId;
	}
	public void setSteamId(String steamID64) {
		this.steamId = steamID64;
	}
	public Set<Game> getOwnedGames() {
		return ownedGames;
	}
	public void setOwnedGames(Set<Game> ownedGames) {
		this.ownedGames = ownedGames;
	}
	public Credentials getCredentials() {
		return credentials;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	public Set<User> getFollowed() {
		return followed;
	}
	public void setFollowed(Set<User> followed) {
		this.followed = followed;
	}
	public Set<User> getFollowers() {
		return followers;
	}
	public void setFollowers(Set<User> followers) {
		this.followers = followers;
	}
	@Override
	public int hashCode() {
		return Objects.hash(email);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email);
	}
	
	public void addFollowed(User b) {
		this.followed.add(b);
	}
	
	public void addFollower(User a) {
		this.followers.add(a);
	}
	
}
