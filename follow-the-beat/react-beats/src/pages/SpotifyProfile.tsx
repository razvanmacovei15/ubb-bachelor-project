// src/pages/SpotifyProfile.tsx
import React from "react";
import SpotifyConnection from "../components/spotify/SpotifyConnection";
import FilterSidebar from "../components/spotify/FilterSidebar";
import useSpotifyProfile from "../hooks/useSpotifyProfile";
import "./ProfilePage.css";
import axios from "axios";
import SpotifyTrackCard from "@/components/spotify/SpotifyTrackCard.tsx";
import SpotifyArtistCard from "@/components/spotify/SpotifyArtistCard.tsx";

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

  const API_URL = import.meta.env.VITE_API_URL;

  const fetchUntoldFestival = async () => {
    try {
      const response = await axios.post(`${API_URL}/api/untold/sync`);
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
      const response = await axios.post(`${API_URL}/api/electric/sync`);
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
      const response = await axios.get(`${API_URL}/api/v1/concerts`, {
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
      const response = await axios.post(`${API_URL}/api/fake-data/festivals`, null, {
        params: {
          count: 100000,
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
                                          <SpotifyArtistCard key={index} spotifyArtistDto={artist}/>
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
                                          <SpotifyTrackCard key={index} spotifyTrackDto={track}/>
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