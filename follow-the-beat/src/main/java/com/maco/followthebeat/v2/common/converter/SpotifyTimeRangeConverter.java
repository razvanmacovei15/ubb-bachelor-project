package com.maco.followthebeat.v2.common.converter;

import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SpotifyTimeRangeConverter implements Converter<String, SpotifyTimeRange> {

    @Override
    public SpotifyTimeRange convert(String source) {
        if (source == null) return null;
        return SpotifyTimeRange.valueOf(source.trim().toUpperCase());
    }
}