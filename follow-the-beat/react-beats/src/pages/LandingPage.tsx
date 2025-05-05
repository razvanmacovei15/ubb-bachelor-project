import { Link } from 'react-router-dom';
import "./LandingPage.css";
import { FaSpotify } from "react-icons/fa";

const LandingPage = () => {
  const features = [
    // {
    //   title: "Discover Concerts",
    //   description: "Find concerts near you using our RapidAPI integration. Search by location and discover your next favorite show.",
    //   icon: "🎵",
    //   link: "/concerts"
    // },
    {
      title: "Explore Festivals",
      description: "Browse through our curated list of festivals. Found a festival we don't have? Suggest it to us!",
      icon: "🎪",
      link: "/festivals"
    },
    {
      title: "My Lineup",
      description: "Keep track of concerts you're interested in and those you've decided to attend. Your personal concert calendar.",
      icon: "📅",
      link: "/lineup"
    },
    {
      title: "Spotify Stats",
      description: "View your top tracks, artists, and generate a personalized listening profile based on your Spotify data.",
      icon: <FaSpotify color="#1db954" />,
      link: "/profile"
    }
  ];

  return (
      <div className="landing-page">
        <div className="hero-section">
          <h1 className="hero-title">Follow The Beat</h1>
          <p className="hero-subtitle">
            Your ultimate music discovery and concert companion
          </p>
        </div>

        <div className="features-grid">
          {features.map((feature) => (
              <div key={feature.title} className="feature-card">
                <div className="feature-icon">{feature.icon}</div>
                <h3>{feature.title}</h3>
                <p>{feature.description}</p>
                <Link to={feature.link} className="feature-link">
                  Explore →
                </Link>
              </div>
          ))}
        </div>

        <div className="coming-soon-section">
          <div className="coming-soon-card">
            <h3>🎯 AI-Powered Recommendations</h3>
            <p>
              Coming soon: Get personalized concert recommendations based on your listening patterns!
            </p>
          </div>
        </div>
      </div>
  );
};

export default LandingPage;
