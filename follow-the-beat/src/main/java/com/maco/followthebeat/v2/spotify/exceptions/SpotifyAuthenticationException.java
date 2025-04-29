package com.maco.followthebeat.v2.spotify.exceptions;

public class SpotifyAuthenticationException extends RuntimeException {
    public SpotifyAuthenticationException(String message) {
        super(message);
    }

    public SpotifyAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
