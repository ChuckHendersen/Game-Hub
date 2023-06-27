package it.uniroma3.siw.GameHub;

import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import org.springframework.stereotype.Component;

// Possono esistere più istanze di SteamAPI

@Component
public class SteamAPI {
	// E' lecito lasciare la chiave web API in chiaro in questo modo??
	
	private SteamWebApiClient client;
	
	public SteamAPI() {
		client = new SteamWebApiClient.SteamWebApiClientBuilder("YOURSTEAMWEBAPIKEY").build();
	}
	
	public SteamWebApiClient getClient() {
		return client;
	}
}
