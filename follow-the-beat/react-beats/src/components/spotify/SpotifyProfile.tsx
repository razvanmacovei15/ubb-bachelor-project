import { useUser } from "../../contexts/UserContext";
import useSpotifyProfile from "../../hooks/useSpotifyProfile";
import SpotifyArtistDto from "../../types/SpotifyArtistDto";
import SpotifyTrackDto from "../../types/SpotifyTrackDto";

const SpotifyProfile = () => {
  const { isConnectedToSpotify, isLoading: userLoading } = useUser();
  const {
    contentLoading,
    statsLoading,
    error,
    topArtists,
    topTracks,
    timeRange,
    currentView,
    setCurrentView,
    handleTimeRangeChange,
    handleSpotifyLogin,
  } = useSpotifyProfile();

  if (userLoading) {
    return <div className="loading-container">Loading...</div>;
  }

  if (!isConnectedToSpotify) {
    return (
      <div className="spotify-connect-container">
        <h2>Connect to Spotify</h2>
        <p>Connect your Spotify account to see your top artists and tracks.</p>
        <button onClick={handleSpotifyLogin} className="spotify-connect-button">
          Connect to Spotify
        </button>
      </div>
    );
  }

  if (contentLoading) {
    return <div className="loading-container">Loading...</div>;
  }

  if (error) {
    return (
      <div className="error-container">
        <h2>Error</h2>
        <p>{error}</p>
        <button onClick={handleSpotifyLogin} className="spotify-connect-button">
          Try Again
        </button>
      </div>
    );
  }

  return (
    <div className="spotify-profile">
      <div className="view-toggle">
        <button
          className={currentView === "artists" ? "active" : ""}
          onClick={() => setCurrentView("artists")}
        >
          Top Artists
        </button>
        <button
          className={currentView === "tracks" ? "active" : ""}
          onClick={() => setCurrentView("tracks")}
        >
          Top Tracks
        </button>
      </div>

      <div className="time-range-selector">
        <button
          className={timeRange === "short_term" ? "active" : ""}
          onClick={() => handleTimeRangeChange("short_term")}
        >
          Last 4 Weeks
        </button>
        <button
          className={timeRange === "medium_term" ? "active" : ""}
          onClick={() => handleTimeRangeChange("medium_term")}
        >
          Last 6 Months
        </button>
        <button
          className={timeRange === "long_term" ? "active" : ""}
          onClick={() => handleTimeRangeChange("long_term")}
        >
          All Time
        </button>
      </div>

      {statsLoading && <div className="loading-overlay">Loading...</div>}

      <div className="content-container">
        {currentView === "artists" ? (
          <div className="artists-grid">
            {topArtists.map((artist: SpotifyArtistDto) => (
              <div key={artist.spotifyId} className="artist-card">
                <img src={artist.imageUrl} alt={artist.name} />
                <h3>{artist.name}</h3>
                <p>Popularity: {artist.popularity}</p>
              </div>
            ))}
          </div>
        ) : (
          <div className="tracks-list">
            {topTracks.map((track: SpotifyTrackDto) => (
              <div key={track.spotifyId} className="track-item">
                <img src={track.albumImgUrl} alt={track.name} />
                <div className="track-info">
                  <h3>{track.name}</h3>
                  <p>{track.artists.map((a) => a.name).join(", ")}</p>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default SpotifyProfile;
