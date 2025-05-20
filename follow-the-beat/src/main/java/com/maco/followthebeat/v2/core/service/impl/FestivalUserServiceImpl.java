package com.maco.followthebeat.v2.core.service.impl;

import com.maco.followthebeat.v2.core.dto.FestivalUserDto;
import com.maco.followthebeat.v2.core.entity.FestivalUser;
import com.maco.followthebeat.v2.core.repo.FestivalUserRepo;
import com.maco.followthebeat.v2.core.service.interfaces.FestivalUserService;
import com.maco.followthebeat.v2.core.mappers.FestivalUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class FestivalUserServiceImpl implements FestivalUserService {

    private final FestivalUserRepo repo;
    private final FestivalUserMapper mapper;

    @Override
    public FestivalUserDto getById(UUID id) {
        return repo.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("FestivalUser not found with id: " + id));
    }

    @Override
    public List<FestivalUserDto> getAll() {
        return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public FestivalUserDto create(FestivalUserDto dto) {
        FestivalUser entity = mapper.toEntity(dto);
        return mapper.toDto(repo.save(entity));
    }

    @Override
    public FestivalUserDto update(UUID id, FestivalUserDto dto) {
        FestivalUser existing = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("FestivalUser not found with id: " + id));
        existing.setGeneratedCompatibility(dto.getGeneratedCompatibility());
        return mapper.toDto(repo.save(existing));
    }

    @Override
    public void delete(UUID id) {
        repo.deleteById(id);
    }

    @Override
    public boolean existsByFestivalIdAndUserId(UUID festivalId, UUID userId) {
        return repo.existsByFestivalIdAndUserId(festivalId, userId);
    }

    @Override
    public FestivalUserDto getByFestivalIdAndUserId(UUID festivalId, UUID userId) {

        log.info("Fetching FestivalUser with festivalId: {} and userId: {}", festivalId, userId);
        return repo.findByFestivalIdAndUserId(festivalId, userId)
                .map(mapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("FestivalUser not found with festivalId: " + festivalId + " and userId: " + userId));
    }
}
