package it.uniroma3.siw.GameHub.repository;

import it.uniroma3.siw.GameHub.model.Follow;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FollowRepository extends CrudRepository<Follow, Long> {
    public Optional<Follow> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    public Iterable<Follow> findAllByFollowerId(Long followerId);

    public Iterable<Follow> findAllByFollowedId(Long followedId);

    public void deleteByFollowerIdAndFollowedId(Long followerId, Long followedId);

}
