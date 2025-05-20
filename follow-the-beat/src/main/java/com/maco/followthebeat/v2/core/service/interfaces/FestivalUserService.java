package com.maco.followthebeat.v2.core.service.interfaces;

import com.maco.followthebeat.v2.core.dto.FestivalUserDto;

import java.util.List;
import java.util.UUID;

public interface FestivalUserService {
    FestivalUserDto getById(UUID id);
    List<FestivalUserDto> getAll();
    FestivalUserDto create(FestivalUserDto dto);
    FestivalUserDto update(UUID id, FestivalUserDto dto);
    void delete(UUID id);
    boolean existsByFestivalIdAndUserId(UUID festivalId, UUID userId);
    FestivalUserDto getByFestivalIdAndUserId(UUID festivalId, UUID userId);
}
