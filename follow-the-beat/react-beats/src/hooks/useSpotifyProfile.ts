// src/hooks/useSpotifyProfile.ts
import {useEffect, useState} from "react";
import axios from "axios";
import SpotifyTrackDto from "../types/SpotifyTrackDto.ts";
import SpotifyArtistDto from "@/types/SpotifyArtistDto.ts";

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

const useSpotifyProfile = () => {
    // const API_URL = import.meta.env.VITE_API_URL;
    const API_URL = "";
    const sessionToken = localStorage.getItem("sessionToken");

    const [contentLoading, setContentLoading] = useState<boolean>(true);
    const [statsLoading, setStatsLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const [timeRange, setTimeRange] = useState<string>("medium_term");
    const [topArtists, setTopArtists] = useState<SpotifyArtistDto[]>([]);
    const [topTracks, setTopTracks] = useState<SpotifyTrackDto[]>([]);
    const [currentView, setCurrentView] = useState<ViewType>("artists");

    const checkSpotifyConnection = async () => {
        if (!sessionToken) {
            return false;
        }

        try {
            const response = await axios.get<boolean>(`${API_URL}/spotify-auth/is-connected`, {
                params: { sessionToken }
            });
            return response.data;
        } catch (err) {
            console.error("Error checking Spotify connection:", err);
            return false;
        }
    };

    const fetchTopArtists = async (range: string) => {
        const response = await axios.get<SpotifyArtistDto[]>(`${API_URL}/api/spotify-artists/top-artists`, {
            headers: { Authorization: `Bearer ${sessionToken}` },
            params: { limit: 50, range },
        });
        console.log("Top artists response:", response.data);
        setTopArtists(response.data);
    };

    const fetchTopTracks = async (range: string) => {
        const response = await axios.get<SpotifyTrackDto[]>(`${API_URL}/api/spotify-tracks/top-tracks`, {
            headers: { Authorization: `Bearer ${sessionToken}` },
            params: { limit: 50, range },
        });
        setTopTracks(response.data);
        console.log("Top tracks response:", response.data);
    };

    const fetchSpotifyData = async (range: string) => {
        await Promise.all([fetchTopArtists(range), fetchTopTracks(range)]);
    };

    const fetchInitialSpotifyData = async () => {
        setContentLoading(true);
        try {
            const isConnected = await checkSpotifyConnection();
            if (isConnected) {
                await fetchSpotifyData(timeRange);
            }
        } catch (err) {
            console.error(err);
            setError("Failed to fetch Spotify data");
        } finally {
            setContentLoading(false);
        }
    };

    const fetchStatsSpotifyData = async (range: string) => {
        setStatsLoading(true);
        try {
            const isConnected = await checkSpotifyConnection();
            if (isConnected) {
                await fetchSpotifyData(range);
            }
        } catch (err) {
            console.error(err);
            setError("Failed to fetch Spotify data");
        } finally {
            setStatsLoading(false);
        }
    };

    const handleSpotifyLogin = async () => {
        const res = await axios.get<string>(`${API_URL}/spotify-auth/auth-url`);
        window.location.href = res.data;
    };

    const handleTimeRangeChange = (newRange: string) => {
        setTimeRange(newRange);
        if (sessionToken) {
            fetchStatsSpotifyData(newRange);
        }
    };

    useEffect(() => {
        const initializeSpotifyData = async () => {
            if (sessionToken) {
                await fetchInitialSpotifyData();
            } else {
                setContentLoading(false);
            }
        };

        initializeSpotifyData();
    }, [sessionToken]);

    return {
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
        checkSpotifyConnection,
    };
};

export default useSpotifyProfile;
