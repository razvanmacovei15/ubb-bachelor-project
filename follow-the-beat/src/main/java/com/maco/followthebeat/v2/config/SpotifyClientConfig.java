package com.maco.followthebeat.v2.config;

import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientFactory;
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
    public SpotifyClientFactory spotifyClientFactory() {
        return new SpotifyClientFactory(clientId, clientSecret, redirectUri, scopes);
    }
} 