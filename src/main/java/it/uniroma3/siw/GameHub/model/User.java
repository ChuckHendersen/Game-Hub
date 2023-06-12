package it.uniroma3.siw.GameHub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    @NotBlank
    private String email;
    @OneToOne
    private Picture foto;
    private String steamId;

    @OneToOne(mappedBy = "user")
    private Credentials credentials;

    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY)
    private Set<Follow> followed;

    @OneToMany(mappedBy = "followed", fetch = FetchType.LAZY)
    private Set<Follow> followers;

    @ManyToMany
    private Set<Game> ownedGames;

    private String steamProfilePictureLink;

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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Set<Follow> getFollowed() {
        return followed;
    }

    public void setFollowed(Set<Follow> followed) {
        this.followed = followed;
    }

    public Set<Follow> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Follow> followers) {
        this.followers = followers;
    }

    public Set<Game> getOwnedGames() {
        return ownedGames;
    }

    public void setOwnedGames(Set<Game> ownedGames) {
        this.ownedGames = ownedGames;
    }

    public boolean isFollower(User user) {
        return this.followers.contains(user);
    }

    public Picture getFoto() {
        return foto;
    }

    public void setFoto(Picture foto) {
        this.foto = foto;
    }

    public String getSteamProfilePictureLink(){
        return steamProfilePictureLink;
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


    public void addFollowed(Follow newFollow) {
        this.followed.add(newFollow);
    }

    public void addFollower(Follow newFollow) {
        this.followers.add(newFollow);
    }

    public void removeFollowed(Follow followToBeDeleted) {
        this.followed.remove(followToBeDeleted);
    }

    public void removeFollower(Follow followToBeDeleted) {
        this.followers.remove(followToBeDeleted);
    }

    public void setSteamProfilePictureLink(String imageLink) {
        this.steamProfilePictureLink = imageLink;
    }
}
