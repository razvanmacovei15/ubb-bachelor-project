package com.maco.followthebeat.v2.user.service.impl;

import com.maco.followthebeat.v2.core.dto.ArtistForSuggestionDto;
import com.maco.followthebeat.v2.core.service.interfaces.ArtistService;
import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.user.dto.UserEmbeddingPayloadDto;
import com.maco.followthebeat.v2.user.dto.UserListeningProfileDto;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.entity.UserListeningProfile;
import com.maco.followthebeat.v2.user.mapper.UserEmbeddingPayloadMapper;
import com.maco.followthebeat.v2.user.mapper.UserListeningProfileMapper;
import com.maco.followthebeat.v2.user.repo.UserListeningProfileRepo;
import com.maco.followthebeat.v2.user.service.interfaces.UserListeningProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
@Slf4j
@Service
@AllArgsConstructor
public class UserListeningProfileServiceImpl implements UserListeningProfileService {
    private final UserListeningProfileRepo repo;
    private final UserEmbeddingPayloadMapper embeddingMapper;
    private final UserListeningProfileMapper mapper;
    private final ArtistService artistService;

    public UserEmbeddingPayloadDto getEmbeddingPayloadForFestival(
             UUID userId,
             SpotifyTimeRange range,
             UUID festivalId
    ) {
        log.info("Requesting embedding payload for festival {} in time range {}", festivalId, range);
        List<ArtistForSuggestionDto> artists = artistService.generateFestivalPayload(festivalId);
        log.info("Artists for festival {}: {}", festivalId, artists);
        return repo.findByUserId(userId)
                .map(profile -> embeddingMapper.toDto(profile, range, artists, userId))
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public List<UserListeningProfileDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    public UserListeningProfileDto findById(UUID id) {
        return repo.findById(id).map(mapper::toDto).orElseThrow();
    }

    @Override
    public UserListeningProfileDto findByUserId(UUID userId) {
        return repo.findByUserId(userId)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public UserListeningProfileDto create(UserListeningProfileDto dto) {
        return mapper.toDto(repo.save(mapper.toEntity(dto)));
    }

    private UserListeningProfile createFreshProfile(User user){
        UserListeningProfile profile = new UserListeningProfile();
        profile.setUser(user);
        return repo.save(profile);
    }

    public UserListeningProfile getOrCreateListeningProfile(User user){
        return repo.findByUserId(user.getId())
                .orElseGet(() -> createFreshProfile(user));
    }

    public UserListeningProfileDto update(UUID id, UserListeningProfileDto dto) {
        UserListeningProfile existing = repo.findById(id).orElseThrow();
        return mapper.toDto(repo.save(existing));
    }

    public void delete(UUID id) {
        repo.deleteById(id);
    }
}
