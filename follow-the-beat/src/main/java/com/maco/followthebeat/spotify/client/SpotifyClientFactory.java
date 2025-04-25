package com.maco.followthebeat.spotify.client;

import com.maco.spotify.api.client.SpotifyClient;
import com.maco.spotify.api.config.SpotifyConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    public SpotifyClient createSpotifyClient() {
        log.info("Creating Spotify client with clientId: {}", clientId);
        SpotifyConfig config = new SpotifyConfig.Builder()
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withRedirectUri(redirectUri)
                .withScopes(scopes)
                .build();

        return new SpotifyClient(config);
    }


}
