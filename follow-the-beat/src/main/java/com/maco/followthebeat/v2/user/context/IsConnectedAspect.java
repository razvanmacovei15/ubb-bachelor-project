package com.maco.followthebeat.v2.user.context;

import com.maco.followthebeat.v2.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
@Slf4j
public class IsConnectedAspect {

    private final UserContext userContext;

    public IsConnectedAspect(UserContext userContext) {
        this.userContext = userContext;
    }

    @Before("@annotation(IsConnected)")
    public void validateUserConnection() {
        log.info("Checking if user is connected to Spotify");
        User user = userContext.getOrThrow();
        log.info("User {} is connected to Spotify: {}", user.getId(), user.isHasSpotifyConnected());
        if (!user.isHasSpotifyConnected()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must have connected Spotify.");
        }
        if(!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be active.");
        }
    }
}
