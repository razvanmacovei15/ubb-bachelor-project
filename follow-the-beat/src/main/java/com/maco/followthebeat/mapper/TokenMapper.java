package com.maco.followthebeat.mapper;

import com.maco.followthebeat.entity.SpotifyUserData;
import com.maco.followthebeat.entity.User;
import com.maco.spotify.api.client.SpotifyClient;
import com.maco.spotify.api.model.SpotifyUser;
import org.springframework.stereotype.Component;

@Component
public class TokenMapper {
    public SpotifyUserData toEntity(SpotifyClient client, User user){

        SpotifyUser userDetails = client.getUserDetails();

        return SpotifyUserData.builder()
                .user(user)
                .spotifyUserId(userDetails.getId())
                .accessToken(client.getTokenManager().getCurrentToken().getAccessToken())
                .expiresIn(client.getTokenManager().getCurrentToken().getExpiresIn())
                .refreshToken(client.getTokenManager().getCurrentToken().getRefreshToken())
                .tokenType(client.getTokenManager().getCurrentToken().getTokenType())
                .scope(client.getTokenManager().getCurrentToken().getScope())
                .build();
    }
}
