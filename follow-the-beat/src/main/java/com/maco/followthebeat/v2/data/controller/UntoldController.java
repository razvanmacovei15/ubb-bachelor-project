package com.maco.followthebeat.v2.data.controller;

import com.maco.followthebeat.v2.data.adapter.untold.UntoldAdapter;
import com.maco.followthebeat.v2.data.scrappers.untold.model.UntoldFestivalResponse;
import com.maco.followthebeat.v2.data.scrappers.api.GenericApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/untold")
@RequiredArgsConstructor
@Slf4j
public class UntoldController {

    private final GenericApi genericApi;
    private final UntoldAdapter untoldAdapter;

    @Value("${scraper.api.untold}")
    private String untoldEndpoint;

    @PostMapping("/sync")
    public ResponseEntity<String> syncUntoldFestival() {
        try {
            UntoldFestivalResponse response = genericApi.fetchFestival(
                    untoldEndpoint,
                    UntoldFestivalResponse.class,
                    "untold"
            );

            if (response == null) {
                return ResponseEntity.badRequest().body("Failed to fetch Untold festival data.");
            }

            untoldAdapter.saveUntoldFestival(response);
            return ResponseEntity.ok("Untold festival data synced successfully.");
        } catch (Exception e) {
            log.error("Error syncing Untold festival", e);
            return ResponseEntity.internalServerError().body("Error syncing Untold festival: " + e.getMessage());
        }
    }
}