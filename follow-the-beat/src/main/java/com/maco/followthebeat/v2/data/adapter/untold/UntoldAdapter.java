package com.maco.followthebeat.v2.data.adapter.untold;

import com.maco.followthebeat.v2.core.entity.*;
import com.maco.followthebeat.v2.core.service.impl.SuperService;
import com.maco.followthebeat.v2.data.adapter.untold.managers.UntoldArtistManager;
import com.maco.followthebeat.v2.data.adapter.untold.managers.UntoldConcertManager;
import com.maco.followthebeat.v2.data.adapter.untold.managers.UntoldFestivalManager;
import com.maco.followthebeat.v2.data.scrappers.untold.model.UntoldFestivalResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UntoldAdapter extends SuperService {

    private final UntoldFestivalManager untoldFestivalManager;
    private final UntoldArtistManager untoldArtistManager;
    private final UntoldConcertManager untoldConcertManager;

    @Transactional
    public void saveUntoldFestival(UntoldFestivalResponse response) {
        Festival festival = untoldFestivalManager.checkOrCreateOrUpdate(response);

        if (untoldArtistManager.isArtistListDifferent(festival, response.getArtists())) {
            log.info("Artist list changed. Refreshing concerts...");
            untoldConcertManager.replaceConcertsForFestival(festival, response);
        } else {
            log.info("Artist list unchanged. No update needed.");
        }
    }
}


