import { useState, useEffect } from "react";
import axios from "axios";
import "./ProfilePage.css";
import { v4 as uuidv4 } from "uuid";


type ParamsType = {
  state: string;
  userId?: string;
};

const ProfilePage = () => {
  const [userData, setUserData] = useState<{
    isSpotifyConnected: boolean;
  } | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const checkSpotifyAuth = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/spotify-artists/top-artists?limit=30&range=LAST_6_MONTHS"
      );
      if (response.status === 200) {
        setUserData({ isSpotifyConnected: true });
        console.log(response.data);
      }
    } catch (err) {
      setUserData({ isSpotifyConnected: false });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    checkSpotifyAuth();
  }, []);

  const handleSpotifyLogin = async () => {
    try {
      setLoading(true);
      setError(null);

      let userId: string | null = localStorage.getItem("userId");
      const state = userId || uuidv4();

      // Prepare the request parameters
      const params: ParamsType = {
        state,
        ...(userId && { userId }),
      };

      // Get the Spotify authorization URL from our backend
      const response = await axios.get(
        "http://localhost:8080/spotify-auth/auth-url",
        { params }
      );
      const authUrl = response.data as string;

      // Extract userId from the URL if not already in local storage
      if (!userId) {
        const urlParams = new URLSearchParams(new URL(authUrl).search);
        userId = urlParams.get("userId");
        if (userId) {
          localStorage.setItem("userId", userId);
        }
      }

      // Open Spotify login in a popup window
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
          console.log("Final user ID:", newUserId);
          localStorage.setItem("userId", newUserId);
        }
      });

      setTimeout(()=> {
        // Listen for the popup window to close
        const checkPopupClosed = setInterval(() => {
          if (popup?.closed) {
            clearInterval(checkPopupClosed);
            checkSpotifyAuth();
          }
        }, 1000);
      }, 1000)

      // Cleanup in case nothing happens for 5 minutes
      setTimeout(() => {
        if (!popup?.closed) popup?.close();
        setLoading(false);
      }, 300000);
    } catch (err) {
      setError("Failed to initiate Spotify login");
      setLoading(false);
    }
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
      <h1>Profile</h1>

      <div className="spotify-section">
        <h2>Spotify Connection</h2>
        {userData?.isSpotifyConnected ? (
          <div className="connected-status">
            <p>✅ Connected to Spotify</p>
            <button
              className="spotify-button"
              onClick={() => window.location.reload()}
            >
              Refresh Connection
            </button>
          </div>
        ) : (
          <div className="spotify-login-container">
            <p>Connect your Spotify account to access your music data</p>
            <button
              className="spotify-button"
              onClick={handleSpotifyLogin}
              disabled={loading}
            >
              {loading ? "Loading..." : "Login with Spotify"}
            </button>
          </div>
        )}
        {error && <p className="error-message">{error}</p>}
      </div>
    </div>
  );
};

export default ProfilePage;
