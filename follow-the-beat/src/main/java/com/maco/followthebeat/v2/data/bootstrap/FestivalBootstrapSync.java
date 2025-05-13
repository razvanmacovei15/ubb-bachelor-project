package com.maco.followthebeat.v2.data.bootstrap;

import com.maco.followthebeat.v2.data.adapter.electriccastle.ElectricAdapter;
import com.maco.followthebeat.v2.data.adapter.untold.UntoldAdapter;
import com.maco.followthebeat.v2.data.scrappers.api.GenericApi;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.model.ElectricFestivalResponse;
import com.maco.followthebeat.v2.data.scrappers.untold.model.UntoldFestivalResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FestivalBootstrapSync {

    private final GenericApi genericApi;
    private final ElectricAdapter electricAdapter;
    private final UntoldAdapter untoldAdapter;

    @Value("${scraper.api.electric}")
    private String electricEndpoint;

    @Value("${scraper.api.untold}")
    private String untoldEndpoint;

    @PostConstruct
    public void initFestivalsOnStartup() {
        log.info("Bootstrapping festival sync...");

        try {
            UntoldFestivalResponse untoldData = genericApi.fetchFestival(
                    untoldEndpoint, UntoldFestivalResponse.class, "untold"
            );
            if (untoldData == null) throw new RuntimeException("Untold data is null");
            untoldAdapter.saveUntoldFestival(untoldData);
            log.info("Untold festival synced.");
        } catch (Exception e) {
            log.error("Failed to sync Untold festival", e);
            throw new RuntimeException("Startup failed: Untold sync error", e);
        }

        try {
            ElectricFestivalResponse electricData = genericApi.fetchFestival(
                    electricEndpoint, ElectricFestivalResponse.class, "electric"
            );
            if (electricData == null) throw new RuntimeException("Electric data is null");
            electricAdapter.saveElectricFestival(electricData);
            log.info("Electric festival synced.");
        } catch (Exception e) {
            log.error("Failed to sync Electric festival", e);
            throw new RuntimeException("Startup failed: Electric sync error", e);
        }

        log.info("Festival bootstrap complete.");
    }
}
