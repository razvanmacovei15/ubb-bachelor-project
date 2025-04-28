package com.maco.followthebeat.v2.spotify.artists.strategy;

import com.maco.followthebeat.v2.spotify.artists.dto.SpotifyArtistDto;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.artists.entity.DbSpotifyArtist;
import com.maco.followthebeat.v2.spotify.artists.entity.BaseUserTopArtist;
import com.maco.followthebeat.v2.spotify.api.SpotifyApiArtistsService;
import com.maco.followthebeat.v2.spotify.artists.mapper.SpotifyArtistMapper;
import com.maco.followthebeat.v2.spotify.artists.service.interfaces.SpotifyArtistService;
import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public abstract class AbstractArtistSaveStrategy<T extends BaseUserTopArtist> implements ArtistSaveStrategy {

    protected final SpotifyApiArtistsService spotifyApiArtistsService;
    protected final SpotifyArtistMapper spotifyArtistMapper;
    protected final SpotifyArtistService spotifyArtistService;

    protected abstract JpaRepository<T, UUID> getRepository();
    protected abstract T createEntity(User user, DbSpotifyArtist artist, int rank);
    protected abstract List<T> findAllByUserOrdered(User user);

    @Override
    public void deleteByUser(User user) {
        findAllByUserOrdered(user).forEach(e -> getRepository().delete(e));
    }

    @Override
    public void saveArtist(User user, DbSpotifyArtist artist, int rank) {
        T entity = createEntity(user, artist, rank);
        getRepository().save(entity);
    }

    @Override
    public List<T> findAllByUser(User user) {
        return findAllByUserOrdered(user);
    }

    @Override
    public void updateStats(User user) {
        List<SpotifyArtistDto> artists = null;
        if (spotifyApiArtistsService != null) {
            artists = spotifyApiArtistsService.fetchTopItems(
                    user.getId(),
                    getTimeRange(),
                    50,
                    0
            );
        }
        if (artists != null) {
            saveArtists(user, artists);
        }
    }

    private void saveArtists(User user, List<SpotifyArtistDto> artists) {
        deleteByUser(user);

        List<T> newArtists = IntStream.range(0, artists.size())
                .mapToObj(index -> {
                    SpotifyArtistDto clientArtist = artists.get(index);
                    DbSpotifyArtist dbSpotifyArtist = null;
                    if (spotifyArtistMapper != null) {
                        dbSpotifyArtist = spotifyArtistMapper.dtoToEntity(clientArtist);
                    }
                    DbSpotifyArtist managedArtist = null;
                    if (spotifyArtistService != null) {
                        managedArtist = spotifyArtistService.saveOrGetExistingArtist(dbSpotifyArtist);
                    }
                    return createEntity(user, managedArtist, index + 1);
                })
                .toList();

        getRepository().saveAll(newArtists);
    }
}
