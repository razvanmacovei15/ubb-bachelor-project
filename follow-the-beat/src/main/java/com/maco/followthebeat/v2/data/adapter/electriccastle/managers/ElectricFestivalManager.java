package com.maco.followthebeat.v2.data.adapter.electriccastle.managers;

import com.maco.followthebeat.v2.core.dto.FestivalDTO;
import com.maco.followthebeat.v2.core.entity.Festival;
import com.maco.followthebeat.v2.core.mappers.FestivalMapper;
import com.maco.followthebeat.v2.core.service.interfaces.FestivalService;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.model.ElectricFestivalResponse;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.service.ElectricService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ElectricFestivalManager {
    private final ElectricService electricService;
    private final FestivalService festivalService;
    private final FestivalMapper festivalMapper;

//    """
//    todo this is one place for improvement
//    this should be universal for any festivals i will have in my app
//    as such i must design the scrapers to return the same type of data
//    no matter what info i can get from scraping
//    just have to adjust to what i have so far
//    this would be a much better solution
//    """

    public Festival checkOrCreateOrUpdate(ElectricFestivalResponse electricFestivalResponse){
        FestivalDTO dto = electricService.createFestivalDTO(electricFestivalResponse);
        Optional<Festival> existingOpt = festivalService.getFestivalByName(dto.getName());

        if(existingOpt.isPresent()){
            Festival existingFestival = existingOpt.get();
            boolean isSame = existingFestival.getStartDate().equals(dto.getStartDate()) &&
                    existingFestival.getEndDate().equals(dto.getEndDate())&&
                    Objects.equals(existingFestival.getLocation(), dto.getLocation()) &&
                    Objects.equals(existingFestival.getDescription(), dto.getDescription());

            if(isSame) return existingFestival;

            existingFestival.setStartDate(dto.getStartDate());
            existingFestival.setEndDate(dto.getEndDate());
            existingFestival.setLocation(dto.getLocation());
            existingFestival.setDescription(dto.getDescription());
            existingFestival.setLogoUrl(dto.getLogoUrl());
            existingFestival.setWebsiteUrl(dto.getWebsiteUrl());
            existingFestival.setIsActive(dto.getIsActive());

            return festivalService.save(existingFestival);
        }
        return festivalService.save(festivalMapper.toEntity(dto));
    }

}
