package com.maco.followthebeat.service.savestrategy;

import com.maco.client.v2.model.SpotifyArtist;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.entity.spotify.DbSpotifyArtist;
import com.maco.followthebeat.entity.spotify.LongTermArtist;
import com.maco.followthebeat.entity.spotify.ShortTermArtist;
import com.maco.followthebeat.entity.spotify.interfaces.BaseUserTopArtist;
import com.maco.followthebeat.enums.SpotifyTimeRange;
import com.maco.followthebeat.mapper.SpotifyArtistMapper;
import com.maco.followthebeat.repo.spotify.LongTermArtistRepository;
import com.maco.followthebeat.service.spotify.interfaces.SpotifyArtistService;
import com.maco.followthebeat.spotify.api.service.artists.SpotifyApiArtistsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class LongTermArtistSaveStrategy implements ArtistSaveStrategy{
    private final LongTermArtistRepository longTermArtistRepository;
    private final SpotifyApiArtistsService spotifyApiArtistsService;
    private final SpotifyArtistMapper spotifyArtistMapper;
    private final SpotifyArtistService spotifyArtistService;
    @Override
    public void deleteByUser(User user) {
        longTermArtistRepository.deleteByUser(user);
    }

    @Override
    public void saveArtist(User user, DbSpotifyArtist artist, int rank) {
        LongTermArtist longTermArtist = new LongTermArtist();
        longTermArtist.setUser(user);
        longTermArtist.setArtist(artist);
        longTermArtist.setRank(rank);
        longTermArtistRepository.save(longTermArtist);
    }

    @Override
    public List<LongTermArtist> findAllByUser(User user) {
        return longTermArtistRepository.findAllByUserOrderByRank(user);
    }

    @Override
    public void updateStats(User user) {
        ResponseEntity<?> response = spotifyApiArtistsService.fetchTopArtists(
                user.getId(),
                SpotifyTimeRange.LONG_TERM,
                50,
                0
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() instanceof List<?> artists) {
            longTermArtistRepository.deleteByUser(user);

            IntStream.range(0, artists.size())
                    .forEach(index -> {
                        SpotifyArtist clientArtist = (SpotifyArtist) artists.get(index);
                        DbSpotifyArtist dbSpotifyArtist = spotifyArtistMapper.clientToEntity(clientArtist);
                        DbSpotifyArtist managedArtist = spotifyArtistService.saveOrGetExistingArtist(dbSpotifyArtist);

                        LongTermArtist longTermArtist = new LongTermArtist();
                        longTermArtist.setUser(user);
                        longTermArtist.setArtist(managedArtist);
                        longTermArtist.setRank(index + 1);
                        longTermArtistRepository.save(longTermArtist);
                    });
        }
    }
}
