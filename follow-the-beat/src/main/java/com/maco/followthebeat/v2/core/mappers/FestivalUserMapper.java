package com.maco.followthebeat.v2.core.mappers;

import com.maco.followthebeat.v2.core.dto.FestivalUserDto;
import com.maco.followthebeat.v2.core.entity.Festival;
import com.maco.followthebeat.v2.core.entity.FestivalUser;
import com.maco.followthebeat.v2.core.service.interfaces.FestivalService;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class FestivalUserMapper {
    private final FestivalService festivalService;
    private final UserService userService;
    public FestivalUserDto toDto(FestivalUser entity) {
        return FestivalUserDto.builder()
                .id(entity.getId())
                .festivalId(entity.getFestival().getId())
                .userId(entity.getUser().getId())
                .generatedCompatibility(entity.getGeneratedCompatibility())
                .timeRange(entity.getTimeRange())
                .build();
    }

    public FestivalUser toEntity(FestivalUserDto dto) {
        Optional<Festival> festival = festivalService.getById(dto.getFestivalId());
        Optional<User> user = userService.findUserById(dto.getUserId());
        return FestivalUser.builder()
                .id(dto.getId())
                .festival(festival.orElseThrow(() -> new IllegalArgumentException("Festival not found with id: " + dto.getFestivalId())))
                .user(user.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + dto.getUserId())))
                .generatedCompatibility(dto.getGeneratedCompatibility())
                .timeRange(dto.getTimeRange())
                .build();
    }

}
