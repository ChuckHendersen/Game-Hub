package it.uniroma3.siw.GameHub.model;

import java.util.Objects;

import jakarta.persistence.*;

@Entity
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Integer steamcode;
	private String gamename;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getSteamcode() {
		return steamcode;
	}
	
	public void setSteamcode(Integer steamcode) {
		this.steamcode = steamcode;
	}
	
	public String getGamename() {
		return gamename;
	}
	
	public void setGamename(String gamename) {
		this.gamename = gamename;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(steamcode);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		return Objects.equals(steamcode, other.steamcode);
	}
	
	@Override
	public String toString() {
		return "appID: "+this.steamcode;
	}
	
	
}
