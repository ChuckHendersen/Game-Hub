package it.uniroma3.siw.GameHub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.friendslist.Friend;
import com.lukaspradel.steamapi.data.json.friendslist.GetFriendList;
import com.lukaspradel.steamapi.webapi.request.GetFriendListRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

import it.uniroma3.siw.GameHub.SteamAPI;

@Controller
public class GeneralController {
	
	@GetMapping("/")
	public String index() throws SteamApiException {
		GetFriendListRequest request = SteamWebApiRequestFactory.createGetFriendListRequest("76561198136135035"); // appId of Dota 2
        GetFriendList getFriendList = SteamAPI.client.<GetFriendList> processRequest(request);
        for(Friend f : getFriendList.getFriendslist().getFriends()) {
        	System.out.println(f.getSteamid());
        }
		return "index.html";
	}
}
