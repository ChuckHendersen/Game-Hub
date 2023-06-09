package it.uniroma3.siw.GameHub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
//Aggiungere constraint di Unicità e Di follow riflessivo

public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    @NotNull
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_id")
    @NotNull
    private User followed;

    public Follow() {
    }

    public Follow(User a, User b) {
        this.follower = a;
        this.followed = b;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowed() {
        return followed;
    }

    public void setFollowed(User followed) {
        this.followed = followed;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Follow follow = (Follow) o;
        return Objects.equals(getFollower(), follow.getFollower()) && Objects.equals(getFollowed(), follow.getFollowed());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFollower(), getFollowed());
    }
}
