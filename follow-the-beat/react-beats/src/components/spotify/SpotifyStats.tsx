import React from 'react';
import './SpotifyStats.css';

interface SpotifyStatsProps {
    topArtists: any[];
    topTracks: any[];
    timeRange: string;
}

const SpotifyStats: React.FC<SpotifyStatsProps> = ({ topArtists, topTracks, timeRange }) => {
    return (
        <div className="spotify-stats">
            <h2>Your Spotify {timeRange} Stats</h2>
            
            <div className="stats-section">
                <h3>Top Artists</h3>
                <div className="artists-grid">
                    {topArtists.slice(0, 5).map((artist, index) => (
                        <div key={artist.id} className="artist-card">
                            <img src={artist.images[0]?.url} alt={artist.name} />
                            <div className="artist-info">
                                <span className="rank">#{index + 1}</span>
                                <h4>{artist.name}</h4>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            <div className="stats-section">
                <h3>Top Tracks</h3>
                <div className="tracks-list">
                    {topTracks.slice(0, 5).map((track, index) => (
                        <div key={track.id} className="track-item">
                            <span className="rank">#{index + 1}</span>
                            <img src={track.album.images[2]?.url} alt={track.name} />
                            <div className="track-info">
                                <h4>{track.name}</h4>
                                <p>{track.artists.map((a: any) => a.name).join(', ')}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default SpotifyStats; 