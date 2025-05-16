package com.maco.followthebeat.v2.user.mapper;

import com.maco.followthebeat.v2.user.dto.UserGenreFrequencyDto;
import com.maco.followthebeat.v2.user.entity.UserGenreFrequency;
import com.maco.followthebeat.v2.user.entity.UserListeningProfile;
import org.springframework.stereotype.Component;

@Component
public class UserGenreFrequencyMapper {
    public UserGenreFrequencyDto toDto(UserGenreFrequency entity) {
        UserGenreFrequencyDto dto = new UserGenreFrequencyDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setGenre(entity.getGenre());
        dto.setCount(entity.getCount());
        dto.setProfileId(entity.getProfile().getId());
        return dto;
    }

    public UserGenreFrequency toEntity(UserGenreFrequencyDto dto, UserListeningProfile profile) {
        UserGenreFrequency entity = new UserGenreFrequency();
        entity.setId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setGenre(dto.getGenre());
        entity.setCount(dto.getCount());
        entity.setProfile(profile);
        return entity;
    }
}