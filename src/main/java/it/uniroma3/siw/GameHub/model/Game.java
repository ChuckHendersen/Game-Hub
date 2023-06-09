package it.uniroma3.siw.GameHub.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"steamcode"}) })
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Integer steamcode;
	private String name;
	
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
	
	public String getName() {
		return name;
	}
	
	public void setName(String gamename) {
		this.name = gamename;
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
