package com.maco.followthebeat.enums;

import lombok.Getter;

@Getter
public enum SpotifyTimeRange {
    SHORT_TERM("short_term"),
    MEDIUM_TERM("medium_term"),
    LONG_TERM("long_term");
    private final String value;

    SpotifyTimeRange(String value) {
        this.value = value;
    }}
