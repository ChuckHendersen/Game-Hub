package it.uniroma3.siw.GameHub;

import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;

// Esiste solo un'istanza di SteamWebApiClient
public class SteamAPI {
	public static SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder("056BDA5087E6B09FF4E875FD1ACAFD3F").build();
}
