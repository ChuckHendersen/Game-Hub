package it.uniroma3.siw.GameHub.service;

import it.uniroma3.siw.GameHub.exceptions.InvalidFollowException;
import it.uniroma3.siw.GameHub.exceptions.UserNotFoundException;
import it.uniroma3.siw.GameHub.model.Follow;
import it.uniroma3.siw.GameHub.model.User;
import it.uniroma3.siw.GameHub.repository.FollowRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowRepository followRepository;

    /**
     * Metodo che consente allo user A di seguire user B
     * @param aId Id dello user che vuole seguire B
     * @param bId Id dello user che viene seguito da A
     * @throws UserNotFoundException se non esiste uno user con id aId o bId
     * @throws InvalidFollowException se A è uguale a B o se A segue già B
     */
    @Transactional
    public void aFollowsB(Long aId, Long bId) throws UserNotFoundException, InvalidFollowException {
        User a = this.userService.findUserById(aId);
        User b = this.userService.findUserById(bId);
        if(!a.equals(b)){
            if(this.followRepository.existsByFollowerIdAndFollowedId(aId, bId)){
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

    /**
     * Metodo che consente allo user A di smettere di seguire user B
     * @param aId Id dello user che vuole smettere di seguire B
     * @param bId Id dello user che viene smesso di seguire da A
     * @throws UserNotFoundException se non esiste uno user con id aId o bId
     * @throws InvalidFollowException se A non segue B oppure prova a smettere di seguire se stesso
     */

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


    /**
     * Metodo che restituisce true se A segue B, false altrimenti
     * @param aId Id dello user di cui dobbiamo verificare il seguito di B
     * @param bId Id dello user di cui dobbiamo verificare il seguito da parte di A
     */
    public boolean aFollowsBBool(Long aId, Long bId) throws UserNotFoundException{
        return this.followRepository.findByFollowerIdAndFollowedId(aId, bId).orElse(null) != null;
    }
}
