package com.maco.followthebeat.v2.common.converters;

import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SpotifyTimeRangeConverter implements Converter<String, SpotifyTimeRange> {

    @Override
    public SpotifyTimeRange convert(String source) {
        return switch (source.toLowerCase()) {
            case "short_term" -> SpotifyTimeRange.SHORT_TERM;
            case "medium_term" -> SpotifyTimeRange.MEDIUM_TERM;
            case "long_term" -> SpotifyTimeRange.LONG_TERM;
            default -> throw new IllegalArgumentException("Unknown SpotifyTimeRange value: " + source);
        };
    }
}
