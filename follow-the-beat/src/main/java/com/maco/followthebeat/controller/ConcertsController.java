package com.maco.followthebeat.controller;

import com.maco.followthebeat.service.ConcertsApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concerts")
public class ConcertsController {

    private final ConcertsApiService concertsApiService;

    public ConcertsController(ConcertsApiService concertsApiService) {
        this.concertsApiService = concertsApiService;
    }

    @GetMapping("/cluj")
    public ResponseEntity<String> getConcertsByArtist() {
        String response = concertsApiService.searchEventsInCluj();
        return ResponseEntity.ok(response);
    }
}
