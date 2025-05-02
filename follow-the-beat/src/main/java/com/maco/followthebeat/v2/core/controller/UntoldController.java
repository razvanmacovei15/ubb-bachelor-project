package com.maco.followthebeat.v2.core.controller;

import com.maco.followthebeat.v2.data.adapter.untold.UntoldAdapter;
import com.maco.followthebeat.v2.data.scrappers.untold.api.UntoldApi;
import com.maco.followthebeat.v2.data.scrappers.untold.model.UntoldFestivalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/api/v1/untold")
@RequiredArgsConstructor
public class UntoldController {
    private final UntoldAdapter untoldAdapter;
    private final UntoldApi untoldApi;
    @GetMapping("/festival")
    public ResponseEntity<String> fetchAndSaveUntoldFestival() {
        try {
            UntoldFestivalResponse untoldFestivalResponse = untoldApi.fetchFestival();
            log.info("Fetched Untold festival data: {}", untoldFestivalResponse);

            if (untoldFestivalResponse != null) {
                untoldAdapter.saveUntoldFestival(untoldFestivalResponse);
                return ResponseEntity.ok("Festival data saved successfully");
            } else {
                return ResponseEntity.status(500).body("Festival data was null or malformed");
            }
        } catch (Exception e) {
            log.error("Exception while processing Untold festival data", e);
            return ResponseEntity.status(500).body("Internal error: " + e.getMessage());
        }
    }




}
