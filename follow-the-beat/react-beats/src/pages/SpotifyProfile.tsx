// src/pages/SpotifyProfile.tsx
import React from "react";
import SpotifyConnection from "../components/spotify/SpotifyConnection";
import FilterSidebar from "../components/spotify/FilterSidebar";
import useSpotifyProfile from "../hooks/useSpotifyProfile";
import "./ProfilePage.css";
import SpotifyTrackCard from "@/components/spotify/SpotifyTrackCard.tsx";
import SpotifyArtistCard from "@/components/spotify/SpotifyArtistCard.tsx";
import {useUser} from "@/contexts/UserContext.tsx";

const SpotifyProfile: React.FC = () => {
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

    const { isConnectedToSpotify } = useUser();

    return (
        <div className="spotify-settings">
            <div className="profile-content">
                <div className="main-content">
                    <SpotifyConnection
                        isConnected={isConnectedToSpotify}
                        loading={contentLoading}
                        error={error}
                        onLogin={handleSpotifyLogin}
                        onRefresh={() => window.location.reload()}
                    />

                    {isConnectedToSpotify && (
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