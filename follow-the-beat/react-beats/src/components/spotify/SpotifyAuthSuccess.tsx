// src/pages/SpotifyAuthSuccess.tsx
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const SpotifyAuthSuccess = () => {
    const navigate = useNavigate();
    const state = new URLSearchParams(window.location.search).get("state");

    useEffect(() => {
        if (!state) return;

        const interval = setInterval(async () => {
            try {
                const res = await axios.get(`http://localhost:8080/spotify-auth/auth-status?state=${state}`);
                if (res.status === 200 && res.data) {
                    localStorage.setItem("sessionToken", res.data);
                    clearInterval(interval);
                    navigate("/profile");
                }
            } catch (err) {
                console.error("Polling error:", err);
            }
        }, 1000);

        return () => clearInterval(interval);
    }, [state, navigate]);

    return <div>Authenticating with Spotify... Please wait.</div>;
};

export default SpotifyAuthSuccess;