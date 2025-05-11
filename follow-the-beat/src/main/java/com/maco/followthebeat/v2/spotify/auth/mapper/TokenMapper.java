package com.maco.followthebeat.v2.spotify.auth.mapper;

import com.maco.client.v2.SpotifyClient;
import com.maco.client.v2.model.SpotifyUser;
import com.maco.followthebeat.v2.spotify.auth.userdata.entity.SpotifyUserData;
import com.maco.followthebeat.v2.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TokenMapper {
    public SpotifyUserData toEntity(SpotifyClient client, User user){

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
