import React, { useState } from 'react';
import { Outlet } from 'react-router-dom';
import './ProfilePage.css';

const ProfilePage: React.FC = () => {
    const [activeTab, setActiveTab] = useState('spotify');

    return (
        <div className="profile">
            <h1>Profile Settings</h1>
            <div className="profile-tabs">
                <button 
                    className={`tab-button ${activeTab === 'spotify' ? 'active' : ''}`}
                    onClick={() => setActiveTab('spotify')}
                >
                    Spotify Stats
                </button>
                <button 
                    className={`tab-button ${activeTab === 'settings' ? 'active' : ''}`}
                    onClick={() => setActiveTab('settings')}
                >
                    Account Settings
                </button>
            </div>
            
            <div className="profile-content">
                {activeTab === 'spotify' ? (
                    <Outlet />
                ) : (
                    <div className="account-settings">
                        <h2>Account Settings</h2>
                        <div className="settings-section">
                            <h3>Profile Information</h3>
                            <div className="form-group">
                                <label>Username</label>
                                <input type="text" placeholder="Enter your username" />
                            </div>
                            <div className="form-group">
                                <label>Email</label>
                                <input type="email" placeholder="Enter your email" />
                            </div>
                            <button className="save-button">Save Changes</button>
                        </div>
                        
                        <div className="settings-section">
                            <h3>Password</h3>
                            <div className="form-group">
                                <label>Current Password</label>
                                <input type="password" placeholder="Enter current password" />
                            </div>
                            <div className="form-group">
                                <label>New Password</label>
                                <input type="password" placeholder="Enter new password" />
                            </div>
                            <button className="save-button">Change Password</button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ProfilePage;
