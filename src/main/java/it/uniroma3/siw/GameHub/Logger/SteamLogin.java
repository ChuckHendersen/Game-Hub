package it.uniroma3.siw.GameHub.Logger;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openid4java.association.AssociationException;
import org.openid4java.consumer.*;
import org.openid4java.discovery.*;
import org.openid4java.message.*;

public class SteamLogin extends ExternalPlatformLogin {
	private static final String STEAM_OPENID = "http://steamcommunity.com/openid";
    private final ConsumerManager manager;
    private final Pattern STEAM_REGEX = Pattern.compile("(\\d+)");
    private DiscoveryInformation discovered;
    
    public SteamLogin() {
    	 System.setProperty("org.apache.commons.logging.Log",
                 "org.apache.commons.logging.impl.NoOpLog");
         manager = new ConsumerManager();
         manager.setMaxAssocAttempts(0);
         try {
             discovered = manager.associate(manager.discover(STEAM_OPENID));
         } catch (DiscoveryException e) {
             e.printStackTrace();
             discovered = null;
         }
    }
    
    public String login(String callbackUrl) {
        if (this.discovered == null) {
            return null;
        }
        try {
            AuthRequest authReq = manager.authenticate(this.discovered, callbackUrl);
            return authReq.getDestinationUrl(true);
        } catch (MessageException | ConsumerException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String verify(String receivingUrl, Map<?,?> responseMap) {
        if (this.discovered == null) {
            return null;
        }
        ParameterList responseList = new ParameterList(responseMap);
        try {
            VerificationResult verification = manager.verify(receivingUrl, responseList, this.discovered);
            Identifier verifiedId = verification.getVerifiedId();
            if (verifiedId != null) {
                String id = verifiedId.getIdentifier();
                System.out.println(id);
                Matcher matcher = STEAM_REGEX.matcher(id);
                if (matcher.find()) {
                    System.out.println();
                    return matcher.group(1);
                }
            }
        } catch (MessageException | DiscoveryException | AssociationException e) {
            e.printStackTrace();
        }
        return null;
    }
}	
