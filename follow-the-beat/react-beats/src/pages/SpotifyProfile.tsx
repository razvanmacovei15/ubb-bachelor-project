// src/pages/SpotifyProfile.tsx
import React from "react";
import SpotifyConnection from "../components/spotify/SpotifyConnection";
import FilterSidebar from "../components/spotify/FilterSidebar";
import useSpotifyProfile from "../hooks/useSpotifyProfile";
import "./ProfilePage.css";
import axios from "axios";

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

  const fetchUntoldFestival = async () => {
    try {
      const response = await axios.post("http://localhost:8080/api/untold/sync");
      console.log("Success:", response.data);
    } catch (error: any) {
      if (error.response) {
        console.error("Server responded with error:", error.response.status, error.response.data);
      } else if (error.request) {
        console.error("No response received:", error.request);
      } else {
        console.error("Error setting up request:", error.message);
      }
    }
  };

  const fetchElectricFestival = async () => {
    try {
      const response = await axios.post("http://localhost:8080/api/electric/sync");
      console.log("Success:", response.data);
    } catch (error: any) {
      if (error.response) {
        console.error("Server responded with error:", error.response.status, error.response.data);
      } else if (error.request) {
        console.error("No response received:", error.request);
      } else {
        console.error("Error setting up request:", error.message);
      }
    }
  };

  const fetchConcerts = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/v1/concerts", {
        params: {
          page: 50,
          size: 50,
          sortBy: "createdAt",
          direction: "desc",
        },
      });

      console.log("Concerts data:", response.data);
    } catch (error) {
      console.error("Error fetching concerts:", error);
    }
  };

  const fetchFakeConcerts = async () => {
    try {
      const response = await axios.post("http://localhost:8080/api/fake-data/festivals", null, {
        params: {
          count: 1000,
        },
      });

      console.log("Fake data generation response:", response.data);
    } catch (error) {
      console.error("Error generating fake concerts:", error);
    }
  };

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

            <button onClick={fetchUntoldFestival}>TEST UNTOLD</button>
            <button onClick={fetchElectricFestival}>TEST ELECTRIC</button>
            <button onClick={fetchConcerts}>TEST FETCH CONCERTS WITH PAGINATION</button>
            <button onClick={fetchFakeConcerts}>TEST CREATING FAKE FESTIVALS</button>

            {userData.isConnectedToSpotify && (
                <>
                  {contentLoading ? (
                      <div className="profile-content-loading">
                        <div className="loading-spinner">Loading your Spotify data...</div>
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
                                <div className="loading-spinner small">Loading stats...</div>
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
                                            <img src={track.albumImgUrl} alt={track.name} />
                                            <div className="track-info">
                                              <h4>{track.name}</h4>
                                              <p>{track.artists.map((a) => a.name).join(", ")}</p>
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
                              onSortChange={(sort) => console.log("Sort changed:", sort)}
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