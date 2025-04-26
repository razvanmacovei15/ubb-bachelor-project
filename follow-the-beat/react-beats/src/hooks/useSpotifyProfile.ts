import { useState, useEffect } from "react";
import axios from "axios";
import { v4 as uuidv4 } from "uuid";
import openSpotifyLoginPopup from "./useSpotifyLoginPopup.ts";

export type ViewType = "artists" | "tracks";

export interface SpotifyArtist {
    id: string;
    name: string;
    images: { url: string }[];
}

export interface SpotifyTrack {
    id: string;
    name: string;
    artists: { name: string }[];
    album: {
        images: { url: string }[];
    };
}

interface UserData {
    isConnectedToSpotify: boolean;
    userId?: string;
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

    const fetchTopArtists = async (userId: string) => {
        const response = await axios.get<SpotifyArtist[]>(`http://localhost:8080/spotify-artists/top-artists`, {
            params: { userId, limit: 50, range: timeRange },
        });
        setTopArtists(response.data);
    };

    const fetchTopTracks = async (userId: string) => {
        const response = await axios.get<SpotifyTrack[]>(`http://localhost:8080/spotify-tracks/top-tracks`, {
            params: { userId, limit: 50, range: timeRange },
        });
        setTopTracks(response.data);
    };

    const fetchSpotifyData = async (userId: string) => {
        await Promise.all([fetchTopArtists(userId), fetchTopTracks(userId)]);
        setUserData({ isConnectedToSpotify: true, userId });
    };

    const fetchInitialSpotifyData = async (userId: string) => {
        setContentLoading(true);
        try {
            await fetchSpotifyData(userId);
        } catch (err) {
            console.error(err);
            setError("Failed to fetch Spotify data");
            setUserData({ isConnectedToSpotify: false });
        } finally {
            setContentLoading(false);
        }
    };

    const fetchStatsSpotifyData = async (userId: string) => {
        setStatsLoading(true);
        try {
            await fetchSpotifyData(userId);
        } catch (err) {
            console.error(err);
            setError("Failed to fetch Spotify data");
        } finally {
            setStatsLoading(false);
        }
    };

    const handleSpotifyLogin = async () => {
        try {
            const localUserId = localStorage.getItem("userId") || uuidv4();
            const authUrl = await axios
                .get<string>("http://localhost:8080/spotify-auth/auth-url", {
                    params: { state: localUserId },
                })
                .then((res) => res.data);

            const newUserId = await openSpotifyLoginPopup(authUrl);
            if (newUserId) {
                localStorage.setItem("userId", newUserId);
                setUserData({ isConnectedToSpotify: true, userId: newUserId });
                await fetchInitialSpotifyData(newUserId);
            }
        } catch (error) {
            console.error(error);
            setError("Spotify login failed. Please try again.");
        }
    };

    const handleTimeRangeChange = (range: string) => {
        setTimeRange(range);
        const storedUserId = localStorage.getItem("userId");
        if (storedUserId) {
            fetchStatsSpotifyData(storedUserId);
        }
    };

    useEffect(() => {
        const storedUserId = localStorage.getItem("userId");
        if (storedUserId) {
            setUserData({ isConnectedToSpotify: true, userId: storedUserId });
            fetchInitialSpotifyData(storedUserId);
        } else {
            setContentLoading(false);
        }
    }, []);

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
