package com.maco.followthebeat.v2.user.controller;

import com.maco.client.v2.SpotifyClient;
import com.maco.client.v2.model.SpotifyArtist;
import com.maco.followthebeat.v2.cache.RedisStateCacheService;
import com.maco.followthebeat.v2.concertmatcher.ConcertMatcherClient;
import com.maco.followthebeat.v2.concertmatcher.MatchResponse;
import com.maco.followthebeat.v2.concertmatcher.MatchResult;
import com.maco.followthebeat.v2.core.dto.ConcertCompatibilityDto;
import com.maco.followthebeat.v2.core.dto.FestivalUserDto;
import com.maco.followthebeat.v2.core.entity.ConcertCompatibility;
import com.maco.followthebeat.v2.core.service.interfaces.ArtistService;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertCompatibilityService;
import com.maco.followthebeat.v2.core.service.interfaces.FestivalUserService;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.user.context.IsConnected;
import com.maco.followthebeat.v2.user.context.UserContext;
import com.maco.followthebeat.v2.user.dto.UserEmbeddingPayloadDto;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.service.interfaces.UserListeningProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@RestController
@RequestMapping("/api/v1/user/suggestions")
@RequiredArgsConstructor
public class UserSuggestionsController {
    private final UserContext userContext;
    private final ConcertMatcherClient concertMatcherClient;
    private final UserListeningProfileService userListeningProfileService;
    private final ConcertCompatibilityService concertCompatibilityService;
    private final FestivalUserService festivalUserService;
    @Qualifier("concertTaskExecutor")
    private final Executor concertTaskExecutor;

    @IsConnected
    @GetMapping
    public ResponseEntity<MatchResponse> getSuggestionsForFestival(
            @RequestParam SpotifyTimeRange range,
            @RequestParam UUID festivalId) {

        User user = userContext.getOrThrow();
        log.info("User {} is requesting suggestions for festival {} with range {}", user.getId(), festivalId, range);

        UserEmbeddingPayloadDto payloadDto = userListeningProfileService.getEmbeddingPayloadForFestival(
                user.getId(),
                range,
                festivalId
        );

        try {
            MatchResponse matchResponse = concertMatcherClient.matchConcerts(payloadDto);

            if (matchResponse.requestId.equals(user.getId())) {
                List<CompletableFuture<Void>> futures = matchResponse.getMatches().stream()
                        .map(match -> CompletableFuture.runAsync(() -> {
                            try {
                                log.info("[AsyncTask] Updating compatibility for concert {}", match.getConcertId());
                                ConcertCompatibilityDto compatibility = concertCompatibilityService.getByConcertAndUser(
                                        UUID.fromString(match.getConcertId()),
                                        user.getId()
                                );
                                compatibility.setCompatibility(match.getScore());
                                concertCompatibilityService.update(compatibility.getId(), compatibility);
                                log.info("[AsyncTask] Done updating concert {}", match.getConcertId());
                            } catch (Exception ex) {
                                log.error("[AsyncTask] Failed to update concert {}: {}", match.getConcertId(), ex.getMessage(), ex);
                            }
                        }, concertTaskExecutor))
                        .toList();

                log.info("Waiting for {} compatibility updates to finish...", futures.size());
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                log.info("All compatibility updates completed.");
            }

            FestivalUserDto festivalUserDto = festivalUserService.getByFestivalIdAndUserId(festivalId, user.getId());
            festivalUserDto.setGeneratedCompatibility(true);
            festivalUserService.update(festivalUserDto.getId(), festivalUserDto);
            log.info("Set generatedCompatibility=true for festivalUser {}", festivalUserDto.getId());

            return ResponseEntity.ok(matchResponse);

        } catch (IOException e) {
            log.error("Concert matcher client failed: {}", e.getMessage(), e);
            throw new RuntimeException("Concert matching failed", e);
        }
    }
}
