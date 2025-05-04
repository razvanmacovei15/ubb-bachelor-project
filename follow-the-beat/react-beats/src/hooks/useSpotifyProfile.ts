// src/hooks/useSpotifyProfile.ts
import { useState, useEffect } from "react";
import axios from "axios";
import { v4 as uuidv4 } from "uuid";

export type ViewType = "artists" | "tracks";

export interface SpotifyArtist {
    id: string;
    name: string;
    imageUrl: string;
    genres: string[];
    popularity: number;
    rank: number;
    playCount: number;
}

export interface SpotifyTrack {
    id: string;
    name: string;
    artists: { name: string }[];
    albumImgUrl: string;
}

interface UserData {
    isConnectedToSpotify: boolean;
}

const useSpotifyProfile = () => {
    const [userData, setUserData] = useState<UserData>({ isConnectedToSpotify: false });
    const [contentLoading, setContentLoading] = useState<boolean>(true);
    const [statsLoading, setStatsLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const [timeRange, setTimeRange] = useState<string>("medium_term");
    const [topArtists, setTopArtists] = useState<SpotifyArtist[]>([]);
    const [topTracks, setTopTracks] = useState<SpotifyTrack[]>([]);
    const [currentView, setCurrentView] = useState<ViewType>("artists");

    const sessionToken = localStorage.getItem("sessionToken");

    const fetchTopArtists = async (range: string) => {
        const response = await axios.get<SpotifyArtist[]>(`http://localhost:8080/api/spotify-artists/top-artists`, {
            headers: { Authorization: `Bearer ${sessionToken}` },
            params: { limit: 50, range },
        });
        setTopArtists(response.data);
    };

    const fetchTopTracks = async (range: string) => {
        const response = await axios.get<SpotifyTrack[]>(`http://localhost:8080/spotify-tracks/top-tracks`, {
            headers: { Authorization: `Bearer ${sessionToken}` },
            params: { limit: 50, range },
        });
        setTopTracks(response.data);
    };

    const fetchSpotifyData = async (range: string) => {
        await Promise.all([fetchTopArtists(range), fetchTopTracks(range)]);
        setUserData({ isConnectedToSpotify: true });
    };

    const fetchInitialSpotifyData = async () => {
        setContentLoading(true);
        try {
            await fetchSpotifyData(timeRange);
        } catch (err) {
            console.error(err);
            setError("Failed to fetch Spotify data");
            setUserData({ isConnectedToSpotify: false });
        } finally {
            setContentLoading(false);
        }
    };

    const fetchStatsSpotifyData = async (range: string) => {
        setStatsLoading(true);
        try {
            await fetchSpotifyData(range);
        } catch (err) {
            console.error(err);
            setError("Failed to fetch Spotify data");
        } finally {
            setStatsLoading(false);
        }
    };

    const handleSpotifyLogin = async () => {
        const state = uuidv4();
        localStorage.setItem("authState", state);

        const authUrl = await axios
            .get("http://localhost:8080/spotify-auth/auth-url", { params: { state } })
            .then((res) => res.data);

        window.location.href = authUrl;
    };

    const handleTimeRangeChange = (newRange: string) => {
        setTimeRange(newRange);
        if (sessionToken) {
            fetchStatsSpotifyData(newRange);
        }
    };

    useEffect(() => {
        if (sessionToken) {
            setUserData({ isConnectedToSpotify: true });
            fetchInitialSpotifyData();
        } else {
            setContentLoading(false);
        }
    }, [sessionToken]);

    return {
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
    };
};

export default useSpotifyProfile;
