import React from "react";
import SpotifyConnection from "../components/spotify/SpotifyConnection";
import FilterSidebar from "../components/spotify/FilterSidebar";
import useSpotifyProfile from "../hooks/useSpotifyProfile";
import "./ProfilePage.css";

const SpotifyProfile: React.FC = () => {
  const {
    userData,
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

  return (
    <div className="spotify-settings">
      <div className="profile-content">
        <div className="main-content">
          <SpotifyConnection
            isConnected={userData.isConnectedToSpotify}
            loading={contentLoading}
            error={error}
            onLogin={handleSpotifyLogin}
            onRefresh={() => window.location.reload()}
          />

          {userData.isConnectedToSpotify && (
            <>
              {contentLoading ? (
                <div className="profile-content-loading">
                  <div className="loading-spinner">
                    Loading your Spotify data...
                  </div>
                </div>
              ) : (
                <>
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

                  <div className="content-wrapper">
                    <div className="stats-container">
                      {statsLoading ? (
                        <div className="loading-spinner small">
                          Loading stats...
                        </div>
                      ) : currentView === "artists" ? (
                        <div className="artists-grid">
                          {topArtists.length === 0 ? (
                            <div className="notfound">
                              <p>Not enough listening data!</p>
                            </div>
                          ) : (
                              topArtists.map((artist, index) => (
                                  <div key={artist.id} className="artist-card">
                                    <img src={artist.imageUrl} alt={artist.name} />
                                    <div className="artist-info">
                                      <span className="rank">#{index + 1}</span>
                                      <h4>{artist.name}</h4>
                                    </div>
                                  </div>
                              ))
                          )}
                        </div>
                      ) : (
                        <div className="tracks-list">
                          {topTracks.length === 0 ? (
                            <div className="notfound">
                              <p>Not enough listening data!</p>
                            </div>
                          ) : (
                            topTracks.map((track, index) => (
                              <div key={track.id} className="track-item">
                                <span className="rank">#{index + 1}</span>
                                <img
                                  src={track.albumImgUrl}
                                  alt={track.name}
                                />
                                <div className="track-info">
                                  <h4>{track.name}</h4>
                                  <p>
                                    {track.artists
                                      .map((a) => a.name)
                                      .join(", ")}
                                  </p>
                                </div>
                              </div>
                            ))
                          )}
                        </div>
                      )}
                    </div>

                    <FilterSidebar
                      timeRange={timeRange}
                      onTimeRangeChange={handleTimeRangeChange}
                      onSortChange={(sort) =>
                        console.log("Sort changed:", sort)
                      }
                    />
                  </div>
                </>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default SpotifyProfile;
