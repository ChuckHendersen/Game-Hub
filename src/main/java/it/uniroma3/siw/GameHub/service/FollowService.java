package it.uniroma3.siw.GameHub.service;

import it.uniroma3.siw.GameHub.exceptions.InvalidFollowException;
import it.uniroma3.siw.GameHub.exceptions.UserNotFoundException;
import it.uniroma3.siw.GameHub.model.Follow;
import it.uniroma3.siw.GameHub.model.User;
import it.uniroma3.siw.GameHub.repository.FollowRepository;
import it.uniroma3.siw.GameHub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;


    /**
     * Metodo che consente allo user A di seguire user B
     * @param aId Id dello user che vuole seguire B
     * @param bId Id dello user che viene seguito da A
     * @return user A se l'operazione ha successo, null altrimenti
     */
    @Transactional
    public void aFollowsB(Long aId, Long bId) throws UserNotFoundException, InvalidFollowException {
        User a = this.userService.findUserById(aId);
        User b = this.userService.findUserById(bId);
        if(!a.equals(b)){
            if(this.followRepository.findByFollowerIdAndFollowedId(aId, bId).orElse(null) != null){
                throw new InvalidFollowException("L'utente segue già l'utente specificato");
            }
            Follow newFollow = new Follow(a, b);
            a.addFollowed(newFollow);
            b.addFollower(newFollow);
            this.followRepository.save(newFollow);
        }else{
            throw new InvalidFollowException("L'utente non può seguire se stesso");
        }
    }

    @Transactional
    public void aUnfollowsB(Long aId, Long bId) throws UserNotFoundException, InvalidFollowException{
        User a = this.userService.findUserById(aId);
        User b = this.userService.findUserById(bId);
        Follow followToBeDeleted = this.followRepository.findByFollowerIdAndFollowedId(aId, bId).orElse(null);
        if(followToBeDeleted == null){
            throw new InvalidFollowException("L'utente non segue l'utente specificato");
        }
        a.removeFollowed(followToBeDeleted);
        b.removeFollower(followToBeDeleted);
        this.followRepository.delete(followToBeDeleted);
    }

}
