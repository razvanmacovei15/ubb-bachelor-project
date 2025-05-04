package com.maco.followthebeat.v2.data.adapter.electriccastle;

import com.maco.followthebeat.v2.core.entity.Festival;
import com.maco.followthebeat.v2.core.service.impl.SuperService;
import com.maco.followthebeat.v2.data.adapter.electriccastle.managers.ElectricArtistManager;
import com.maco.followthebeat.v2.data.adapter.electriccastle.managers.ElectricConcertManager;
import com.maco.followthebeat.v2.data.adapter.electriccastle.managers.ElectricFestivalManager;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.model.ElectricFestivalResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ElectricAdapter{
    private final ElectricFestivalManager electricFestivalManager;
    private final ElectricArtistManager electricArtistManager;
    private final ElectricConcertManager electricConcertManager;

    @Transactional
    public void saveElectricFestival(ElectricFestivalResponse electricFestivalResponse){
        Festival festival = electricFestivalManager.checkOrCreateOrUpdate(electricFestivalResponse);

        if(electricArtistManager.isArtistListDifferent(festival, electricFestivalResponse.getArtists())){
            log.info("Artis list changed. Refreshing concerts...");
            electricConcertManager.replaceConcertsForFestival(festival, electricFestivalResponse);
        } else {
            log.info("Artist list unchanged. No update needed.");
        }
    }
}
