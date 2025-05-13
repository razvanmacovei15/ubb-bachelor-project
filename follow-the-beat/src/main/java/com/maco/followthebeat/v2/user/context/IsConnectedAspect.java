package com.maco.followthebeat.v2.user.context;

import com.maco.followthebeat.v2.user.entity.User;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IsConnectedAspect {

    private final UserContext userContext;

    public IsConnectedAspect(UserContext userContext) {
        this.userContext = userContext;
    }

    @Before("@annotation(IsConnected)")
    public void validateUserConnection() {
        User user = userContext.getOrThrow();
        if (!user.isHasSpotifyConnected()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must have connected Spotify.");
        }
        if(!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be active.");
        }
    }
}
