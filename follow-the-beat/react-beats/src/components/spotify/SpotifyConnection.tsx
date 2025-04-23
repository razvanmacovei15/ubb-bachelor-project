import React from 'react';
import './SpotifyConnection.css';

interface SpotifyConnectionProps {
    isConnected: boolean;
    loading: boolean;
    error: string | null;
    onLogin: () => void;
    onRefresh: () => void;
}

const SpotifyConnection: React.FC<SpotifyConnectionProps> = ({
    isConnected,
    loading,
    error,
    onLogin,
    onRefresh
}) => {
    return (
        <div className="spotify-connection">
            <h2>Spotify Connection</h2>
            {isConnected ? (
                <div className="connected-status">
                    <p>✅ Connected to Spotify</p>
                    <button 
                        className="spotify-button"
                        onClick={onRefresh}
                    >
                        Refresh Connection
                    </button>
                </div>
            ) : (
                <div className="spotify-login-container">
                    <p>Connect your Spotify account to access your music data</p>
                    <button 
                        className="spotify-button"
                        onClick={onLogin}
                        disabled={loading}
                    >
                        {loading ? 'Loading...' : 'Login with Spotify'}
                    </button>
                </div>
            )}
            {error && <p className="error-message">{error}</p>}
        </div>
    );
};

export default SpotifyConnection; 