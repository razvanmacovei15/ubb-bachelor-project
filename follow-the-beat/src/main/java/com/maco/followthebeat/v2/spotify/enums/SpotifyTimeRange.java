package com.maco.followthebeat.v2.spotify.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SpotifyTimeRange {
    SHORT_TERM("short_term"),
    MEDIUM_TERM("medium_term"),
    LONG_TERM("long_term");
    private final String value;

    SpotifyTimeRange(String value) {
        this.value = value;
    }

    public static SpotifyTimeRange fromString(String value) {
        return Arrays.stream(SpotifyTimeRange.values())
                .filter(range -> range.name().equalsIgnoreCase(value.replace("-", "_")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid SpotifyTimeRange: " + value));
    }
}
