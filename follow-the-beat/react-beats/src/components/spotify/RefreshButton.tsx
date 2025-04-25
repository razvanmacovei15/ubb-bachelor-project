import React, { useState } from 'react';
import axios from 'axios';

interface RefreshButtonProps {
    onRefreshComplete?: () => void;
}

const RefreshButton: React.FC<RefreshButtonProps> = ({ onRefreshComplete }) => {
    const [isRefreshing, setIsRefreshing] = useState(false);
    const [lastRefreshed, setLastRefreshed] = useState<Date | null>(null);

    const handleRefresh = async () => {
        try {
            setIsRefreshing(true);
            await axios.post('/api/spotify/refresh');
            setLastRefreshed(new Date());
            onRefreshComplete?.();
        } catch (error) {
            console.error('Error refreshing data:', error);
        } finally {
            setIsRefreshing(false);
        }
    };

    return (
        <div className="refresh-container">
            <button
                onClick={handleRefresh}
                disabled={isRefreshing}
                className={`refresh-button ${isRefreshing ? 'refreshing' : ''}`}
            >
                {isRefreshing ? 'Refreshing...' : 'Refresh Data'}
            </button>
            {lastRefreshed && (
                <p className="last-refreshed">
                    Last refreshed: {lastRefreshed.toLocaleTimeString()}
                </p>
            )}
        </div>
    );
};

export default RefreshButton; 