package com.maco.followthebeat.service;

import com.maco.followthebeat.config.ConcertsApiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class ConcertsApiService {

    private final ConcertsApiConfig config;
    private final RestTemplate restTemplate = new RestTemplate();

    public String searchEventsInCluj() {
        String url = UriComponentsBuilder.fromHttpUrl(config.getBaseUrl() + "/search")
                .queryParam("city", "London")
                .queryParam("types", "event")
                .queryParam("types", "festival")
                .queryParam("radius", "50")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", config.getKey());
        headers.set("X-RapidAPI-Host", config.getHost());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}
