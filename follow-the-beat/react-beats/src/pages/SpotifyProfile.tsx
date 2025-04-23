import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { v4 as uuidv4 } from "uuid";
import SpotifyConnection from '../components/spotify/SpotifyConnection';
import FilterSidebar from '../components/spotify/FilterSidebar';
import './ProfilePage.css';

type ParamsType = {
  state: string;
  userId?: string;
};

interface SpotifyArtist {
  id: string;
  name: string;
  images: { url: string }[];
  popularity: number;
}

interface SpotifyTrack {
  id: string;
  name: string;
  artists: { name: string }[];
  album: {
    images: { url: string }[];
  };
  popularity: number;
}

type ViewType = "artists" | "tracks";

const SpotifyProfile: React.FC = () => {
  const [userData, setUserData] = useState<{
    isSpotifyConnected: boolean;
  } | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [timeRange, setTimeRange] = useState("LAST_6_MONTHS");
  const [topArtists, setTopArtists] = useState<SpotifyArtist[]>([]);
  const [topTracks, setTopTracks] = useState<SpotifyTrack[]>([]);
  const [currentView, setCurrentView] = useState<ViewType>("artists");

  const fetchSpotifyData = async () => {
    try {
      const [artistsResponse, tracksResponse] = await Promise.all([
        axios.get<SpotifyArtist[]>(`http://localhost:8080/spotify-artists/top-artists?limit=30&range=${timeRange}`),
        axios.get<SpotifyTrack[]>(`http://localhost:8080/spotify-tracks/top-tracks?limit=50&range=${timeRange}`)
      ]);

      if (artistsResponse.status === 200 && tracksResponse.status === 200) {
        setTopArtists(artistsResponse.data);
        setTopTracks(tracksResponse.data);
        setUserData({ isSpotifyConnected: true });
      }
    } catch (err) {
      setUserData({ isSpotifyConnected: false });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSpotifyData();
  }, [timeRange]);

  const handleSpotifyLogin = async () => {
    try {
      setLoading(true);
      setError(null);

      let userId: string | null = localStorage.getItem("userId");
      const state = userId || uuidv4();

      const params: ParamsType = {
        state,
        ...(userId && { userId }),
      };

      const response = await axios.get(
        "http://localhost:8080/spotify-auth/auth-url",
        { params }
      );
      const authUrl = response.data as string;

      if (!userId) {
        const urlParams = new URLSearchParams(new URL(authUrl).search);
        userId = urlParams.get("userId");
        if (userId) {
          localStorage.setItem("userId", userId);
        }
      }

      const width = 600;
      const height = 600;
      const left = window.screen.width / 2 - width / 2;
      const top = window.screen.height / 2 - height / 2;

      const popup = window.open(
        authUrl,
        "Spotify Login",
        `width=${width},height=${height},left=${left},top=${top}`
      );

      window.addEventListener("message", (event) => {
        if (event.data?.userId) {
          const newUserId = event.data.userId;
          localStorage.setItem("userId", newUserId);
        }
      });

      setTimeout(() => {
        const checkPopupClosed = setInterval(() => {
          if (popup?.closed) {
            clearInterval(checkPopupClosed);
            fetchSpotifyData();
          }
        }, 1000);
      }, 1000);

      setTimeout(() => {
        if (!popup?.closed) popup?.close();
        setLoading(false);
      }, 300000);
    } catch (err) {
      setError("Failed to initiate Spotify login");
      setLoading(false);
    }
  };

  const handleTimeRangeChange = (range: string) => {
    setTimeRange(range);
  };

  const handleSortChange = (sortBy: string) => {
    // Implement sorting logic here
    console.log("Sorting by:", sortBy);
  };

  const handleViewChange = (view: ViewType) => {
    setCurrentView(view);
  };

  if (loading) {
    return (
      <div className="profile">
        <div className="loading-spinner">Loading...</div>
      </div>
    );
  }

  return (
    <div className="profile">
      <div className="profile-content">
        <div className="main-content">
          <SpotifyConnection
            isConnected={userData?.isSpotifyConnected || false}
            loading={loading}
            error={error}
            onLogin={handleSpotifyLogin}
            onRefresh={() => window.location.reload()}
          />

          {userData?.isSpotifyConnected && (
            <>
              <div className="view-toggle">
                <button 
                  className={currentView === "artists" ? "active" : ""}
                  onClick={() => handleViewChange("artists")}
                >
                  Top Artists
                </button>
                <button 
                  className={currentView === "tracks" ? "active" : ""}
                  onClick={() => handleViewChange("tracks")}
                >
                  Top Tracks
                </button>
              </div>
              
              <div className="stats-container">
                {currentView === "artists" && (
                  <div className="stats-section full-width">
                    <div className="artists-grid">
                      {topArtists.map((artist, index) => (
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
                )}

                {currentView === "tracks" && (
                  <div className="stats-section full-width">
                    <div className="tracks-list">
                      {topTracks.map((track, index) => (
                        <div key={track.id} className="track-item">
                          <span className="rank">#{index + 1}</span>
                          <img src={track.album.images[0]?.url} alt={track.name} />
                          <div className="track-info">
                            <h4>{track.name}</h4>
                            <p>{track.artists.map(a => a.name).join(', ')}</p>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                )}
              </div>
            </>
          )}
        </div>

        {userData?.isSpotifyConnected && (
          <FilterSidebar
            timeRange={timeRange}
            onTimeRangeChange={handleTimeRangeChange}
            onSortChange={handleSortChange}
          />
        )}
      </div>
    </div>
  );
};

export default SpotifyProfile; 