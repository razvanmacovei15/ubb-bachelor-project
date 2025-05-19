// src/pages/SpotifyAuthSuccess.tsx
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useUser } from "../../contexts/UserContext";

const SpotifyAuthSuccess = () => {
  const navigate = useNavigate();
  const { setConnectionState } = useUser();
  const state = new URLSearchParams(window.location.search).get("state");
  const API_URL = import.meta.env.VITE_API_URL;

  useEffect(() => {
    console.log(
      "[SpotifyAuthSuccess] Starting auth success flow with state:",
      state
    );

    if (!state) {
      console.log(
        "[SpotifyAuthSuccess] No state parameter found, redirecting to profile"
      );
      navigate("/profile");
      return;
    }

    const interval = setInterval(async () => {
      try {
        console.log(
          "[SpotifyAuthSuccess] Polling auth status for state:",
          state
        );
        const res = await axios.get<string>(
          `${API_URL}/spotify-auth/auth-status?state=${state}`
        );
        console.log(
          "[SpotifyAuthSuccess] Auth status response:",
          res.status,
          res.data
        );

        if (res.status === 200 && res.data) {
          console.log(
            "[SpotifyAuthSuccess] Session token received, updating context and localStorage"
          );
          setConnectionState(true, res.data);
          axios.defaults.headers.common["Authorization"] = `Bearer ${res.data}`;
          clearInterval(interval);
          console.log("[SpotifyAuthSuccess] Redirecting to profile page");
          navigate("/profile");
        } else {
          console.log("[SpotifyAuthSuccess] No session token in response");
        }
      } catch (err) {
        console.error("[SpotifyAuthSuccess] Polling error:", err);
        clearInterval(interval);
        console.log(
          "[SpotifyAuthSuccess] Error occurred, redirecting to profile"
        );
        navigate("/profile");
      }
    }, 1000);

    // Clear interval after 5 minutes to prevent infinite polling
    const timeout = setTimeout(() => {
      console.log("[SpotifyAuthSuccess] Polling timeout reached");
      clearInterval(interval);
      navigate("/profile");
    }, 300000);

    return () => {
      console.log("[SpotifyAuthSuccess] Cleaning up intervals");
      clearInterval(interval);
      clearTimeout(timeout);
    };
  }, [state, navigate, setConnectionState]);

  return <div>Authenticating with Spotify... Please wait.</div>;
};

export default SpotifyAuthSuccess;
