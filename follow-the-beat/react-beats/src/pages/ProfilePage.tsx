import  { useState, useEffect } from 'react';
import axios from 'axios';
import './ProfilePage.css';
import { v4 as uuidv4 } from 'uuid';


const ProfilePage = () => {
    const [userData, setUserData] = useState<{ isSpotifyConnected: boolean } | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const checkSpotifyAuth = async () => {
        try {
            const response = await axios.get('http://localhost:8080/spotify/top-tracks?limit=10');
            if (response.status === 200) {
                setUserData({ isSpotifyConnected: true });
                console.log(response.data)
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

            const state = uuidv4();
            
            // Get the Spotify authorization URL from our backend
            const response = await axios.get('http://localhost:8080/spotify/auth-url', {
                params: {
                    state,
                }
            });
            const authUrl = response.data as string;
            
            // Open Spotify login in a popup window
            const width = 600;
            const height = 600;
            const left = window.screen.width / 2 - width / 2;
            const top = window.screen.height / 2 - height / 2;
            
            const popup = window.open(
                authUrl,
                'Spotify Login',
                `width=${width},height=${height},left=${left},top=${top}`
            );

            // Listen for the popup window to close
            const checkPopupClosed = setInterval(() => {
                if (popup?.closed) {
                    clearInterval(checkPopupClosed);
                    // Check if authentication was successful
                    checkSpotifyAuth();
                }
            }, 1000);

            // Stop checking after 5 minutes
            setTimeout(() => {
                clearInterval(checkPopupClosed);
                setLoading(false);
            }, 300000);

        } catch (err) {
            setError('Failed to initiate Spotify login');
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
                            {loading ? 'Loading...' : 'Login with Spotify'}
                        </button>
                    </div>
                )}
                {error && <p className="error-message">{error}</p>}
            </div>
        </div>
    );
}

export default ProfilePage;