package it.uniroma3.siw.GameHub.controller;

import java.io.IOException;
import java.net.URI;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.GameHub.Logger.ExternalPlatformLogin;
import it.uniroma3.siw.GameHub.Logger.SteamLogin;

@Controller
public class GeneralController {
	
	
	
	@GetMapping("/")
	public String index(){
		return "index.html";
	}
	
	@GetMapping("/redirection")
	public String redirection() {
		return "redirect:www.google.com";
	}
	
	@GetMapping("/login/steam")
	public String steamLogin(Model model) {
		ExternalPlatformLogin externalLogin = new SteamLogin();
		
		return null;
	}
	
	
	
}
