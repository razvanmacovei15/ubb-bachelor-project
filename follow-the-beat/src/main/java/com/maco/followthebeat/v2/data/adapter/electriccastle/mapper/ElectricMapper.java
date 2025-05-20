package com.maco.followthebeat.v2.data.adapter.electriccastle.mapper;

import com.maco.followthebeat.v2.core.dto.ArtistDTO;
import com.maco.followthebeat.v2.core.dto.FestivalDTO;
import com.maco.followthebeat.v2.core.dto.StageDTO;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.config.ElectricConstants;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.model.ElectricArtist;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.model.ElectricFestivalResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;
@Component
public class ElectricMapper {

    private LocalDate parseStringDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy");
        formatter = formatter.withLocale(Locale.ENGLISH);
        return LocalDate.parse(date, formatter);
    }
    private boolean checkCurrentDateInRange(LocalDate startDate, LocalDate endDate) {
        LocalDate currentDate = LocalDate.now();
        return (currentDate.isAfter(startDate) || currentDate.isEqual(startDate)) && (currentDate.isBefore(endDate) || currentDate.isEqual(endDate));
    }
    public FestivalDTO mapToFestivalDTO(ElectricFestivalResponse electricFestivalResponse) {
        LocalDate startDate = parseStringDate(electricFestivalResponse.getStartDate());
        LocalDate endDate = parseStringDate(electricFestivalResponse.getEndDate());

        return FestivalDTO.builder()
                .name(electricFestivalResponse.getFestivalName())
                .description(ElectricConstants.FESTIVAL_DESCRIPTION)
                .location(electricFestivalResponse.getLocation())
                .startDate(startDate)
                .endDate(endDate)
                .logoUrl(ElectricConstants.LOGO)
                .websiteUrl(ElectricConstants.WEBSITE)
                .isActive(checkCurrentDateInRange(startDate, endDate))
                .festivalImageUrl(ElectricConstants.FESTIVAL_IMG_URL)
                .build();
    }

    public ArtistDTO mapToArtistDTO(ElectricArtist electricArtist) {
        return ArtistDTO.builder()
                .name(electricArtist.getName())
                .imgUrl(electricArtist.getImgUrl())
                .genres(Collections.emptyList())
                .build();
    }

    public StageDTO mapToStageDTO(ElectricArtist electricArtist, FestivalDTO festivalDTO) {
        return StageDTO.builder()
                .name(electricArtist.getStage())
                .festivalDTO(festivalDTO)
                .build();
    }
}
