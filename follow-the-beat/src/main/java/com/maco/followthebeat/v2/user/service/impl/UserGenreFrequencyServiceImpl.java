package com.maco.followthebeat.v2.user.service.impl;

import com.maco.followthebeat.v2.spotify.artists.entity.BaseUserTopArtist;
import com.maco.followthebeat.v2.spotify.artists.entity.ShortTermArtist;
import com.maco.followthebeat.v2.spotify.artists.service.interfaces.SpotifyArtistStatsService;
import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.user.context.IsConnected;
import com.maco.followthebeat.v2.user.context.UserContext;
import com.maco.followthebeat.v2.user.dto.UserGenreFrequencyDto;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.entity.UserGenreFrequency;
import com.maco.followthebeat.v2.user.entity.UserListeningProfile;
import com.maco.followthebeat.v2.user.mapper.UserGenreFrequencyMapper;
import com.maco.followthebeat.v2.user.repo.UserGenreFrequencyRepo;
import com.maco.followthebeat.v2.user.repo.UserListeningProfileRepo;
import com.maco.followthebeat.v2.user.service.interfaces.UserGenreFrequencyService;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserGenreFrequencyServiceImpl implements UserGenreFrequencyService {
    private final UserGenreFrequencyRepo genreFrequencyRepo;
    private final UserListeningProfileRepo profileRepo;
    private final UserGenreFrequencyMapper mapper;
    private final SpotifyArtistStatsService spotifyArtistStatsService;
    private final UserContext userContext;

    public List<UserGenreFrequencyDto> findAll() {
        return genreFrequencyRepo.findAll().stream().map(mapper::toDto).toList();
    }

    public UserGenreFrequencyDto findById(UUID id) {
        return genreFrequencyRepo.findById(id).map(mapper::toDto).orElseThrow();
    }

    public UserGenreFrequencyDto create(UserGenreFrequencyDto dto) {
        UserListeningProfile profile = profileRepo.findById(dto.getProfileId()).orElseThrow();
        return mapper.toDto(genreFrequencyRepo.save(mapper.toEntity(dto, profile)));
    }

    public UserGenreFrequencyDto update(UUID id, UserGenreFrequencyDto dto) {
        UserGenreFrequency entity = genreFrequencyRepo.findById(id).orElseThrow();
        entity.setGenre(dto.getGenre());
        entity.setCount(dto.getCount());
        return mapper.toDto(genreFrequencyRepo.save(entity));
    }

    public void delete(UUID id) {
        genreFrequencyRepo.deleteById(id);
    }
    @IsConnected
    @Transactional
    @Override
    public void generateAndSave(UserListeningProfile profile) {
        //Step 0.0: get the user
        User user = userContext.getOrThrow();

        // Step 0: get all the Lists
        List<? extends BaseUserTopArtist> shortTermArtists = spotifyArtistStatsService.getArtistStatsByTimeRange(user, SpotifyTimeRange.SHORT_TERM);
        List<? extends BaseUserTopArtist> mediumTermArtists = spotifyArtistStatsService.getArtistStatsByTimeRange(user, SpotifyTimeRange.MEDIUM_TERM);
        List<? extends BaseUserTopArtist> longTermArtists = spotifyArtistStatsService.getArtistStatsByTimeRange(user, SpotifyTimeRange.LONG_TERM);


        // Step 1: Merge all lists
        List<BaseUserTopArtist> allArtists = Stream.concat(
                Stream.concat(
                        shortTermArtists.stream(),
                        mediumTermArtists.stream()
                ),
                longTermArtists.stream()
        ).toList();

        // Step 2: Count genres
        Map<String, Integer> genreMap = new HashMap<>();
        for (BaseUserTopArtist artist : allArtists) {
            List<String> genres = artist.getArtist().getGenres();
            if (genres != null) {
                for (String genre : genres) {
                    genreMap.merge(genre.toLowerCase(), 1, Integer::sum);
                }
            }
        }

        // Step 3: Clear old frequencies if updating
        profile.getGenres().clear();

        // Step 4: Convert to entities and attach to profile
        List<UserGenreFrequency> frequencies = genreMap.entrySet().stream()
                .map(entry -> {
                    UserGenreFrequency freq = new UserGenreFrequency();
                    freq.setProfile(profile);
                    freq.setUserId(user.getId());
                    freq.setGenre(entry.getKey());
                    freq.setCount(entry.getValue());
                    return freq;
                })
                .toList();

        profile.getGenres().addAll(frequencies);
        profileRepo.save(profile);
    }


}