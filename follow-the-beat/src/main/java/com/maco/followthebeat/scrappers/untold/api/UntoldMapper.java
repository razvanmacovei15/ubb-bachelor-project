package com.maco.followthebeat.scrappers.untold.api;

import com.maco.followthebeat.feature.base.dto.ArtistDTO;
import com.maco.followthebeat.feature.base.dto.FestivalDTO;
import com.maco.followthebeat.scrappers.untold.api.UntoldArtist;
import com.maco.followthebeat.scrappers.untold.api.UntoldConstants;
import com.maco.followthebeat.scrappers.untold.api.UntoldFestivalResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;

@Component
public class UntoldMapper {
    public ArtistDTO mapArtistDTO(UntoldArtist artist) {
        return ArtistDTO.builder()
                .name(artist.getName())
                .imgUrl(artist.getImageUrl())
                .genres(Collections.emptyList())
                .build();
    }

    private boolean checkCurrentDateInRange(LocalDate startDate, LocalDate endDate) {
        LocalDate currentDate = LocalDate.now();
        return (currentDate.isAfter(startDate) || currentDate.isEqual(startDate)) && (currentDate.isBefore(endDate) || currentDate.isEqual(endDate));
    }

    public FestivalDTO mapFestivalDTO(UntoldFestivalResponse untoldFestivalResponse, LocalDate[] dateRange) {
        FestivalDTO festivalDTO = new FestivalDTO();
        festivalDTO.setName(untoldFestivalResponse.getFestivalName());
        festivalDTO.setDescription(UntoldConstants.FESTIVAL_DESCRIPTION);
        festivalDTO.setLocation(untoldFestivalResponse.getLocation()); //todo implement a more complex location object
        festivalDTO.setStartDate(dateRange[0]);
        festivalDTO.setEndDate(dateRange[1]);
        festivalDTO.setLogoUrl(UntoldConstants.LOGO); //todo later, need to scrape it
        festivalDTO.setWebsiteUrl(UntoldConstants.WEBSITE);
        festivalDTO.setIsActive(checkCurrentDateInRange(dateRange[0], dateRange[1]));
        return festivalDTO;
    }

}
