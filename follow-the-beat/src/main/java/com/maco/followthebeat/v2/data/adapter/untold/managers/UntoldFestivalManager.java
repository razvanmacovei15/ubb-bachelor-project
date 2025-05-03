package com.maco.followthebeat.v2.data.adapter.untold.managers;

import com.maco.followthebeat.v2.core.dto.FestivalDTO;
import com.maco.followthebeat.v2.core.entity.Festival;
import com.maco.followthebeat.v2.core.mappers.FestivalMapper;
import com.maco.followthebeat.v2.core.service.interfaces.FestivalService;
import com.maco.followthebeat.v2.data.scrappers.untold.service.UntoldService;
import com.maco.followthebeat.v2.data.scrappers.untold.model.UntoldFestivalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UntoldFestivalManager {
    private final UntoldService untoldService;
    private final FestivalService festivalService;
    private final FestivalMapper festivalMapper;

    public Festival checkOrCreateOrUpdate(UntoldFestivalResponse response) {
        FestivalDTO dto = untoldService.createFestival(response);
        Optional<Festival> existingOpt = festivalService.getFestivalByName(dto.getName());

        if (existingOpt.isPresent()) {
            Festival existing = existingOpt.get();

            boolean isSame = existing.getStartDate().equals(dto.getStartDate()) &&
                    existing.getEndDate().equals(dto.getEndDate()) &&
                    Objects.equals(existing.getLocation(), dto.getLocation()) &&
                    Objects.equals(existing.getDescription(), dto.getDescription());

            if (isSame) return existing;

            existing.setStartDate(dto.getStartDate());
            existing.setEndDate(dto.getEndDate());
            existing.setLocation(dto.getLocation());
            existing.setDescription(dto.getDescription());
            existing.setLogoUrl(dto.getLogoUrl());
            existing.setWebsiteUrl(dto.getWebsiteUrl());
            existing.setIsActive(dto.getIsActive());

            return festivalService.save(existing);
        }
        return festivalService.save(festivalMapper.toEntity(dto));
    }
}
