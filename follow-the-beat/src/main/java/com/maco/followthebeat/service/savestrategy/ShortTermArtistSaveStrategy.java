package com.maco.followthebeat.service.savestrategy;

import com.maco.client.v2.model.SpotifyArtist;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.entity.spotify.DbSpotifyArtist;
import com.maco.followthebeat.entity.spotify.ShortTermArtist;
import com.maco.followthebeat.entity.spotify.interfaces.BaseUserTopArtist;
import com.maco.followthebeat.enums.SpotifyTimeRange;
import com.maco.followthebeat.mapper.SpotifyArtistMapper;
import com.maco.followthebeat.repo.spotify.ShortTermArtistRepository;
import com.maco.followthebeat.service.spotify.interfaces.SpotifyArtistService;
import com.maco.followthebeat.spotify.api.service.artists.SpotifyApiArtistsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ShortTermArtistSaveStrategy implements ArtistSaveStrategy{
    private final ShortTermArtistRepository shortTermArtistRepository;
    private final SpotifyApiArtistsService spotifyApiArtistsService;
    private final SpotifyArtistMapper spotifyArtistMapper;
    private final SpotifyArtistService spotifyArtistService;
    @Override
    public void deleteByUser(User user) {
        shortTermArtistRepository.deleteByUser(user);
    }

    @Override
    public void saveArtist(User user, DbSpotifyArtist artist, int rank) {
        ShortTermArtist shortTermArtist = new ShortTermArtist();
        shortTermArtist.setUser(user);
        shortTermArtist.setArtist(artist);
        shortTermArtist.setRank(rank);
        shortTermArtistRepository.save(shortTermArtist);
    }

    @Override
    public List<ShortTermArtist> findAllByUser(User user) {
        return shortTermArtistRepository.findAllByUserOrderByRank(user);
    }

    @Override
    public void updateStats(User user) {
        ResponseEntity<?> response = spotifyApiArtistsService.fetchTopArtists(
                user.getId(),
                SpotifyTimeRange.SHORT_TERM,
                50,
                0
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() instanceof List<?> artists) {
            shortTermArtistRepository.deleteByUser(user);

            IntStream.range(0, artists.size())
                    .forEach(index -> {
                        SpotifyArtist clientArtist = (SpotifyArtist) artists.get(index);
                        DbSpotifyArtist dbSpotifyArtist = spotifyArtistMapper.clientToEntity(clientArtist);
                        DbSpotifyArtist managedArtist = spotifyArtistService.saveOrGetExistingArtist(dbSpotifyArtist);

                        ShortTermArtist shortTermArtist = new ShortTermArtist();
                        shortTermArtist.setUser(user);
                        shortTermArtist.setArtist(managedArtist);
                        shortTermArtist.setRank(index + 1);
                        shortTermArtistRepository.save(shortTermArtist);
                    });
        }
    }
}
