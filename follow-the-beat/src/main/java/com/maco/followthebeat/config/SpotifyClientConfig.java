package com.maco.followthebeat.config;

import com.maco.spotify.api.client.SpotifyClient;
import com.maco.spotify.api.config.SpotifyConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpotifyClientConfig {

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.redirect.uri}")
    private String redirectUri;

    @Value("${spotify.scopes}")
    private String scopes;

    @Bean
    public SpotifyClient spotifyClient() {
        SpotifyConfig config = new SpotifyConfig.Builder()
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withRedirectUri(redirectUri)
                .withScopes(scopes.split(","))
                .build();
        
        return new SpotifyClient(config);
    }
} 