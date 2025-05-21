package com.maco.followthebeat.v2.common.converter;

import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SpotifyTimeRangeConverter implements Converter<String, SpotifyTimeRange> {

    @Override
    public SpotifyTimeRange convert(@NotNull String source) {
        return SpotifyTimeRange.valueOf(source.trim().toUpperCase());
    }
}