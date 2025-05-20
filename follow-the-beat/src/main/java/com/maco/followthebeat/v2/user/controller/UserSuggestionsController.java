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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
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

    @IsConnected
    @GetMapping
    public ResponseEntity<MatchResponse> getSuggestionsForFestival(
            @RequestParam SpotifyTimeRange range,
            @RequestParam UUID festivalId) {
        log.info("Requesting suggestions for festival {} in time range {}", festivalId, range);
        User user = userContext.getOrThrow();

        log.info("User {} is requesting suggestions for festival {} in time range {}", user.getId(), festivalId, range);
        UserEmbeddingPayloadDto payloadDto = userListeningProfileService.getEmbeddingPayloadForFestival(
                user.getId(),
                range,
                festivalId
        );
        try {
            MatchResponse matchResponse = concertMatcherClient.matchConcerts(payloadDto);
            if (matchResponse.requestId.equals(user.getId())) {
                List<MatchResult> matches = matchResponse.matches;
                for (MatchResult match : matches) {
                    log.info("Match result: {} with score {}", match.concertId, match.score);
                    ConcertCompatibilityDto existingCompatibilityDto = concertCompatibilityService.getByConcertAndUser(
                            UUID.fromString(match.concertId),
                            user.getId()
                    );

                    existingCompatibilityDto.setCompatibility(match.score);
                    concertCompatibilityService.update(existingCompatibilityDto.getId(), existingCompatibilityDto);
                }
                log.info("All match results: {}", matchResponse.getMatches());
            }
            FestivalUserDto festivalUserDto = festivalUserService.getById(festivalId);
            festivalUserDto.setGeneratedCompatibility(true);
            festivalUserService.update(festivalUserDto.getId(), festivalUserDto);
            return ResponseEntity.ok(matchResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
