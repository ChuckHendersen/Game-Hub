package it.uniroma3.siw.GameHub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.friendslist.Friend;
import com.lukaspradel.steamapi.data.json.friendslist.Friendslist;
import com.lukaspradel.steamapi.data.json.friendslist.GetFriendList;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetFriendListRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

@Controller
public class GeneralController {
	
	private static SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder("056BDA5087E6B09FF4E875FD1ACAFD3F").build();
	
	@GetMapping("/")
	public String index() throws SteamApiException {
		GetFriendListRequest request = SteamWebApiRequestFactory.createGetFriendListRequest("76561198136135035");
		GetFriendList answer = client.<GetFriendList>processRequest(request);
		Friendslist fl = answer.getFriendslist();
		for(Friend f : fl.getFriends()) {
			System.out.println("Amico: "+f.getSteamid());
		}
		return "index.html";
	}
}
