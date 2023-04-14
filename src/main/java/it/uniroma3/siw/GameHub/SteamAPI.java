package it.uniroma3.siw.GameHub;

import org.springframework.stereotype.Component;

import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;

// Esiste solo un'istanza di SteamWebApiClient

@Component
public class SteamAPI {
	// E' lecito lasciare la chiave web API in chiaro in questo modo??
	
	private SteamWebApiClient client;
	
	public SteamAPI() {
		client = new SteamWebApiClient.SteamWebApiClientBuilder("056BDA5087E6B09FF4E875FD1ACAFD3F").build();
	}
	
	public SteamWebApiClient getClient() {
		return client;
	}
}
