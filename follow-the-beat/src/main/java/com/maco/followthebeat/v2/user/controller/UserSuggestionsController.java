package com.maco.followthebeat.v2.user.controller;

import com.maco.client.v2.SpotifyClient;
import com.maco.client.v2.model.SpotifyArtist;
import com.maco.followthebeat.v2.cache.RedisStateCacheService;
import com.maco.followthebeat.v2.core.service.interfaces.ArtistService;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.user.context.IsConnected;
import com.maco.followthebeat.v2.user.context.UserContext;
import com.maco.followthebeat.v2.user.dto.UserEmbeddingPayloadDto;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.service.interfaces.UserListeningProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
@Slf4j
@RestController
@RequestMapping("/api/v1/user/suggestions")
@RequiredArgsConstructor
public class UserSuggestionsController {
    private final UserContext userContext;
    private final RedisStateCacheService redisStateCacheService;
    private final UserListeningProfileService userListeningProfileService;
    private final SpotifyClientManager clientManager;

    @IsConnected
    @GetMapping UserEmbeddingPayloadDto getSuggestionsForFestival(SpotifyTimeRange range, UUID festivalId) {
        log.info("Requesting suggestions for festival {} in time range {}", festivalId, range);
        User user = userContext.getOrThrow();

        log.info("User {} is requesting suggestions for festival {} in time range {}", user.getId(), festivalId, range);
        UserEmbeddingPayloadDto payloadDto = userListeningProfileService.getEmbeddingPayloadForFestival(
                user.getId(),
                range,
                festivalId
        );
        // -> generate artist information like genres first DONE
        // -> send it to the AI api to create a suggestion
        // -> get a response from the AI api
        // -> update the artist <-> user information
        // -> return the response
        return payloadDto;
    }

}
