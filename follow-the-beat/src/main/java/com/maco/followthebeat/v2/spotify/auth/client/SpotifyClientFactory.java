package com.maco.followthebeat.v2.spotify.auth.client;

import com.maco.client.v2.SpotifyClientI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class SpotifyClientFactory {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String[] scopes;

    public SpotifyClientFactory(
            @Value("${spotify.client.id}") String clientId,
            @Value("${spotify.client.secret}") String clientSecret,
            @Value("${spotify.redirect.uri}") String redirectUri,
            @Value("${spotify.scopes}") String scopes) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scopes = scopes.split(",");
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SpotifyClientI createSpotifyClient() {
        return new SpotifyClientI(clientId, clientSecret, redirectUri, scopes);
    }

    //todo
    //user context
    //filters -> chain of responsabilty -> request thread local bean -> user ocntext
    // session scope
    //validate access token -> get user details() -> save details in DB
}
