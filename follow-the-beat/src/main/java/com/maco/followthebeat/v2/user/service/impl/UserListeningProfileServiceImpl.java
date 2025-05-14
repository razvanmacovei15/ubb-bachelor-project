package com.maco.followthebeat.v2.user.service.impl;

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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
@Service
@AllArgsConstructor
public class UserListeningProfileServiceImpl implements UserListeningProfileService {
    private final UserListeningProfileRepo repo;
    private final UserEmbeddingPayloadMapper embeddingMapper;
    private final UserListeningProfileMapper mapper;

    public UserEmbeddingPayloadDto getEmbeddingPayload(
             UUID id,
             SpotifyTimeRange range
    ) {
        return repo.findByUserId(id)
                .map(profile -> embeddingMapper.toDto(profile, range))
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
                .orElseGet(() -> {
                    return createFreshProfile(user);
                });
    }

    public UserListeningProfileDto update(UUID id, UserListeningProfileDto dto) {
        UserListeningProfile existing = repo.findById(id).orElseThrow();
        return mapper.toDto(repo.save(existing));
    }

    public void delete(UUID id) {
        repo.deleteById(id);
    }
}
