package com.maco.followthebeat.v2.user.service.interfaces;

import com.maco.followthebeat.v2.user.dto.UserListeningProfileDto;
import com.maco.followthebeat.v2.user.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserListeningProfileService {
    List<UserListeningProfileDto> findAll();
    UserListeningProfileDto findById(UUID id);
    UserListeningProfileDto findByUserId(UUID userId);
    UserListeningProfileDto create(UserListeningProfileDto dto);
    UserListeningProfileDto getOrCreateListeningProfile(User user);
    UserListeningProfileDto update(UUID id, UserListeningProfileDto dto);
    void delete(UUID id);

}