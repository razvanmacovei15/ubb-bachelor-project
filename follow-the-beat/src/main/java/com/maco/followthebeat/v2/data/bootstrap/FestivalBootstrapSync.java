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

import java.util.concurrent.TimeUnit;

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

    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 5000; // 5 seconds

    @PostConstruct
    public void initFestivalsOnStartup() {
        log.info("Bootstrapping festival sync...");
        
        syncUntoldFestival();
        syncElectricFestival();
        
        log.info("Festival bootstrap complete.");
    }

    private void syncUntoldFestival() {
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            try {
                UntoldFestivalResponse untoldData = genericApi.fetchFestival(
                        untoldEndpoint, UntoldFestivalResponse.class, "untold"
                );
                if (untoldData == null) throw new RuntimeException("Untold data is null");
                untoldAdapter.saveUntoldFestival(untoldData);
                log.info("Untold festival synced successfully.");
                return;
            } catch (Exception e) {
                retryCount++;
                if (retryCount == MAX_RETRIES) {
                    log.error("Failed to sync Untold festival after {} attempts. The site might be under maintenance. Continuing without Untold data.", MAX_RETRIES, e);
                    return;
                }
                log.warn("Failed to sync Untold festival (attempt {}/{}). Retrying in {} seconds...", retryCount, MAX_RETRIES, RETRY_DELAY_MS/1000);
                try {
                    TimeUnit.MILLISECONDS.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private void syncElectricFestival() {
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            try {
                ElectricFestivalResponse electricData = genericApi.fetchFestival(
                        electricEndpoint, ElectricFestivalResponse.class, "electric"
                );
                if (electricData == null) throw new RuntimeException("Electric data is null");
                electricAdapter.saveElectricFestival(electricData);
                log.info("Electric festival synced successfully.");
                return;
            } catch (Exception e) {
                retryCount++;
                if (retryCount == MAX_RETRIES) {
                    log.error("Failed to sync Electric festival after {} attempts. The site might be under maintenance. Continuing without Electric data.", MAX_RETRIES, e);
                    return;
                }
                log.warn("Failed to sync Electric festival (attempt {}/{}). Retrying in {} seconds...", retryCount, MAX_RETRIES, RETRY_DELAY_MS/1000);
                try {
                    TimeUnit.MILLISECONDS.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}
