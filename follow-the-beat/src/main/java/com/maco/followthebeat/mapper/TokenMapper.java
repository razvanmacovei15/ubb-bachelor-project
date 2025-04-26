package com.maco.followthebeat.mapper;

import com.maco.client.v2.SpotifyClientI;
import com.maco.client.v2.model.SpotifyUser;
import com.maco.followthebeat.entity.SpotifyUserData;
import com.maco.followthebeat.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TokenMapper {
    public SpotifyUserData toEntity(SpotifyClientI client, User user){

        SpotifyUser userDetails = client.getCurrentUserDetails();

        return SpotifyUserData.builder()
                .user(user)
                .spotifyUserId(userDetails.getId())
                .accessToken(client.getAccessToken().getAccessToken())
                .expiresIn(client.getAccessToken().getExpiresIn())
                .refreshToken(client.getAccessToken().getRefreshToken())
                .tokenType(client.getAccessToken().getTokenType())
                .scope(client.getAccessToken().getScope())
                .build();
    }
}
