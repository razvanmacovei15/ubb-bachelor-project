package com.maco.followthebeat.v2.user.service.interfaces;

import com.maco.followthebeat.v2.spotify.artists.entity.BaseUserTopArtist;
import com.maco.followthebeat.v2.spotify.artists.entity.LongTermArtist;
import com.maco.followthebeat.v2.spotify.artists.entity.MediumTermArtist;
import com.maco.followthebeat.v2.spotify.artists.entity.ShortTermArtist;
import com.maco.followthebeat.v2.user.dto.UserGenreFrequencyDto;
import com.maco.followthebeat.v2.user.entity.UserListeningProfile;

import java.util.List;
import java.util.UUID;

public interface UserGenreFrequencyService {
    List<UserGenreFrequencyDto> findAll();
    UserGenreFrequencyDto findById(UUID id);
    UserGenreFrequencyDto create(UserGenreFrequencyDto dto);
    UserGenreFrequencyDto update(UUID id, UserGenreFrequencyDto dto);
    void delete(UUID id);
    void generateAndSave(UserListeningProfile profile);
}