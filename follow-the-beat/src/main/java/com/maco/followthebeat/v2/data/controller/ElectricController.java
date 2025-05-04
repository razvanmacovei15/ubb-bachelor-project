package com.maco.followthebeat.v2.data.controller;

import com.maco.followthebeat.v2.data.adapter.electriccastle.ElectricAdapter;
import com.maco.followthebeat.v2.data.scrappers.api.GenericApi;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.model.ElectricFestivalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/electric")
@RequiredArgsConstructor
@Slf4j
public class ElectricController {
    private final GenericApi genericApi;
    private final ElectricAdapter electricAdapter;

    @Value("${scraper.api.electric}")
    private String electricEndpoint;

    @PostMapping("/sync")
    public ResponseEntity<String> syncElectricFestival(){
        try {
            ElectricFestivalResponse electricFestivalResponse = genericApi.fetchFestival(
                    electricEndpoint,
                    ElectricFestivalResponse.class,
                    "electric"
            );

            if (electricFestivalResponse == null) {
                return ResponseEntity.badRequest().body("Failed to fetch Untold festival data.");
            }

            electricAdapter.saveElectricFestival(electricFestivalResponse);
            return ResponseEntity.ok("Electric festival data synced successfully.");

        } catch (Exception e){
            log.error("Error syncing Electric festival", e);
            return ResponseEntity.internalServerError().body("Error syncing Electric festival: " + e.getMessage());
        }
    }
}
