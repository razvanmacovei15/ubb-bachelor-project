package com.maco.followthebeat.service.savestrategy;

import com.maco.client.v2.model.SpotifyArtist;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.entity.spotify.DbSpotifyArtist;
import com.maco.followthebeat.entity.spotify.MediumTermArtist;
import com.maco.followthebeat.entity.spotify.ShortTermArtist;
import com.maco.followthebeat.enums.SpotifyTimeRange;
import com.maco.followthebeat.mapper.SpotifyArtistMapper;
import com.maco.followthebeat.repo.spotify.MediumTermArtistRepository;
import com.maco.followthebeat.service.spotify.interfaces.SpotifyArtistService;
import com.maco.followthebeat.spotify.api.service.artists.SpotifyApiArtistsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MediumTermArtistSaveStrategy implements ArtistSaveStrategy{
    private final MediumTermArtistRepository mediumTermArtistRepository;
    private final SpotifyApiArtistsService spotifyApiArtistsService;
    private final SpotifyArtistMapper spotifyArtistMapper;
    private final SpotifyArtistService spotifyArtistService;
    @Override
    public void deleteByUser(User user) {
        mediumTermArtistRepository.deleteByUser(user);
    }

    @Override
    public void saveArtist(User user, DbSpotifyArtist artist, int rank) {
        MediumTermArtist mediumTermArtist = new MediumTermArtist();
        mediumTermArtist.setUser(user);
        mediumTermArtist.setArtist(artist);
        mediumTermArtist.setRank(rank);
        mediumTermArtistRepository.save(mediumTermArtist);
    }

    @Override
    public List<MediumTermArtist> findAllByUser(User user) {
        return mediumTermArtistRepository.findAllByUserOrderByRank(user);
    }

    @Override
    public void updateStats(User user) {
        ResponseEntity<?> response = spotifyApiArtistsService.fetchTopArtists(
                user.getId(),
                SpotifyTimeRange.MEDIUM_TERM,
                50,
                0
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() instanceof List<?> artists) {
            mediumTermArtistRepository.deleteByUser(user);

            IntStream.range(0, artists.size())
                    .forEach(index -> {
                        SpotifyArtist clientArtist = (SpotifyArtist) artists.get(index);
                        DbSpotifyArtist dbSpotifyArtist = spotifyArtistMapper.clientToEntity(clientArtist);
                        DbSpotifyArtist managedArtist = spotifyArtistService.saveOrGetExistingArtist(dbSpotifyArtist);

                        MediumTermArtist mediumTermArtist = new MediumTermArtist();
                        mediumTermArtist.setUser(user);
                        mediumTermArtist.setArtist(managedArtist);
                        mediumTermArtist.setRank(index + 1);
                        mediumTermArtistRepository.save(mediumTermArtist);
                    });
        }
    }
}
