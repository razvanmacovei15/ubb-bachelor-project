package com.maco.followthebeat.v2.data.adapter.electriccastle.managers;

import com.maco.followthebeat.v2.core.entity.Festival;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertService;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.model.ElectricArtist;
import com.maco.followthebeat.v2.data.scrappers.untold.model.UntoldArtist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class ElectricArtistManager {

    private final ConcertService concertService;

    public boolean isArtistListDifferent(Festival festival, List<ElectricArtist> apiArtists) {
        Set<String> existing = concertService.getConcertsByFestivalId(festival.getId())
                .stream()
                .map(c -> c.getArtist().getName())
                .collect(Collectors.toSet());

        Set<String> incoming = apiArtists.stream()
                .map(ElectricArtist::getName)
                .collect(Collectors.toSet());

        return !existing.equals(incoming);
    }
}
