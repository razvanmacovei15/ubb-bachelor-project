package com.maco.followthebeat.v2.user.service.interfaces;

import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.user.dto.UserEmbeddingPayloadDto;
import com.maco.followthebeat.v2.user.dto.UserListeningProfileDto;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.entity.UserListeningProfile;

import java.util.List;
import java.util.UUID;

public interface UserListeningProfileService {
    UserEmbeddingPayloadDto getEmbeddingPayloadForFestival(
            UUID userId,
            SpotifyTimeRange range,
            UUID festivalId
    );
    List<UserListeningProfileDto> findAll();
    UserListeningProfileDto findById(UUID id);
    UserListeningProfileDto findByUserId(UUID userId);
    UserListeningProfileDto create(UserListeningProfileDto dto);
    UserListeningProfile getOrCreateListeningProfile(User user);
    UserListeningProfileDto update(UUID id, UserListeningProfileDto dto);
    void delete(UUID id);

}