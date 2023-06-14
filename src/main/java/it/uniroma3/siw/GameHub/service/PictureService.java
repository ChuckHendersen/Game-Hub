package it.uniroma3.siw.GameHub.service;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.playersummaries.GetPlayerSummaries;
import com.lukaspradel.steamapi.webapi.request.GetPlayerSummariesRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;
import it.uniroma3.siw.GameHub.SteamAPI;
import it.uniroma3.siw.GameHub.exceptions.InvalidUserOperationException;
import it.uniroma3.siw.GameHub.exceptions.UserNotFoundException;
import it.uniroma3.siw.GameHub.model.Picture;
import it.uniroma3.siw.GameHub.model.User;
import it.uniroma3.siw.GameHub.repository.PictureRepository;
import it.uniroma3.siw.GameHub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PictureService {

    @Autowired
    private SteamAPI steamApi;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private CredentialsService credentialsService;

    @Transactional
    public void updateUserImageFromSteam(Long userId) throws UserNotFoundException, SteamApiException, InvalidUserOperationException {
        this.credentialsService.checkCurrentUserIsAuthorized(userId);
        User user = this.userService.findUserById(userId);
        if (user.getSteamId() != null) {
            GetPlayerSummariesRequest request = SteamWebApiRequestFactory.createGetPlayerSummariesRequest(List.of(user.getSteamId()));
            GetPlayerSummaries answer = steamApi.getClient().<GetPlayerSummaries>processRequest(request);
            String imageLink = answer.getResponse().getPlayers().get(0).getAvatarfull();
            user.setSteamProfilePictureLink(imageLink);
            Picture daCancellare = user.getFoto();
            user.setFoto(null);
            userRepository.save(user);
            if(daCancellare != null)
                this.pictureRepository.delete(daCancellare);
        }
    }

    @Transactional
    public void updateUserImageFromFile(Long userId, MultipartFile file) throws UserNotFoundException, IOException, InvalidUserOperationException {
        this.credentialsService.checkCurrentUserIsAuthorized(userId);
        User user= this.userService.findUserById(userId);
        Picture daCancellare = user.getFoto();
        Picture nuova= new Picture();
        nuova.setNome(file.getOriginalFilename());
        nuova.setFoto(file.getBytes());
        this.pictureRepository.save(nuova);
        user.setFoto(nuova);
        user.setSteamProfilePictureLink(null);
        userRepository.save(user);
        if(daCancellare != null)
            this.pictureRepository.delete(daCancellare);

    }
}
