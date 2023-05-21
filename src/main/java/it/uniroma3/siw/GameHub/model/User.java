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
	
	@ManyToMany
	private Set<User> friendList;
	
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
	
	public Set<User> friendList(){
		return friendList;
	}
	
	public void setFriendList(Set<User>friendList) {
		this.friendList = friendList;
	}
	
	public Set<Game> getOwnedGames() {
		return ownedGames;
	}
	public void setOwnedGames(Set<Game> ownedGames) {
		this.ownedGames = ownedGames;
	}
	public Set<User> getFriendList() {
		return friendList;
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
	
}
