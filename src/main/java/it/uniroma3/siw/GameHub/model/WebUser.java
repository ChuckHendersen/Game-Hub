package it.uniroma3.siw.GameHub.model;

import java.util.Objects;

import jakarta.persistence.*;

@Entity
public class WebUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String username;
	private String userEmail;
	private String steamID64;
	
	//private List<WebUser> friendList;
	
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
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getSteamID64() {
		return steamID64;
	}
	public void setSteamID64(String steamID64) {
		this.steamID64 = steamID64;
	}
	
//	public List<WebUser> friendList(){
//		return friendList;
//	}
//	
//	public void setFriendList(List<WebUser>friendList) {
//		this.friendList = friendList;
//	}
	
	@Override
	public int hashCode() {
		return Objects.hash(userEmail);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof WebUser))
			return false;
		WebUser other = (WebUser) obj;
		return Objects.equals(userEmail, other.userEmail);
	}
	
}
