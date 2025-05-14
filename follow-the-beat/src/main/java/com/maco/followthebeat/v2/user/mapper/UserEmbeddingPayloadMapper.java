package com.maco.followthebeat.v2.user.mapper;

import com.maco.followthebeat.v2.spotify.artists.entity.BaseUserTopArtist;
import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.user.dto.UserEmbeddingPayloadDto;
import com.maco.followthebeat.v2.user.entity.UserGenreFrequency;
import com.maco.followthebeat.v2.user.entity.UserListeningProfile;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UserEmbeddingPayloadMapper {
    public UserEmbeddingPayloadDto toDto(UserListeningProfile profile, SpotifyTimeRange timeRange) {
        UserEmbeddingPayloadDto dto =  new UserEmbeddingPayloadDto();
        List<String> topArtists = switch(timeRange){
            case SHORT_TERM -> extractArtistNames(profile.getShortTermArtists());
            case MEDIUM_TERM -> extractArtistNames(profile.getMediumTermArtists());
            case LONG_TERM -> extractArtistNames(profile.getLongTermArtists());
        };

        Map<String, Integer> genreMap = profile.getGenres().stream()
                .collect(Collectors.toMap(
                    UserGenreFrequency::getGenre,
                    UserGenreFrequency::getCount
                ));

        dto.setTopArtists(topArtists);
        dto.setGenreFrequencies(genreMap);
        return dto;
    }

    private List<String> extractArtistNames(List<? extends BaseUserTopArtist> artists) {
        return artists.stream()
                .sorted(Comparator.comparing(BaseUserTopArtist::getRank))
                .limit(15)
                .map(a -> a.getArtist().getName())
                .toList();
    }
}
