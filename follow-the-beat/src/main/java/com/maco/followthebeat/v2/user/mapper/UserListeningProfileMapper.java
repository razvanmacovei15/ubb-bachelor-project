package com.maco.followthebeat.v2.user.mapper;

import com.maco.followthebeat.v2.user.context.UserContext;
import com.maco.followthebeat.v2.user.dto.UserListeningProfileDto;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.entity.UserListeningProfile;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserListeningProfileMapper {
    private final UserContext userContext;
    public UserListeningProfileDto toDto(UserListeningProfile entity) {
        UserListeningProfileDto dto = new UserListeningProfileDto();
        dto.setUserId(entity.getUser().getId());
        return dto;
    }

    public UserListeningProfile toEntity(UserListeningProfileDto dto) {
        UserListeningProfile entity = new UserListeningProfile();
        User user = userContext.getOrThrow();
        entity.setUser(user);
        return entity;
    }
}
