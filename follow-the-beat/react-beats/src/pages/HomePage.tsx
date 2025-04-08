import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useConcertContext } from "../components/contexts/ConcertContext";
import "./HomePage.css";
import TopBar from "../components/top-bar/TopBar";

const HomePage: React.FC = () => {
  const [isSearching, setIsSearching] = useState(false);
  const [searchInput, setSearchInput] = useState("");
  const { setConcerts } = useConcertContext();
  const navigate = useNavigate();

  const handleSearch = async () => {
    setIsSearching(true);

    await new Promise((res) => setTimeout(res, 3000));

    const mockConcerts = Array.from({ length: 20 }, (_, i) => ({
      id: i + 1,
      artist: `Artist ${i + 1}`,
      location: searchInput || `Cluj-Napoca`,
      startTime: new Date(Date.now() + i * 3600000).toISOString(),
      compatibility: Math.floor(Math.random() * 101),
    }));

    setConcerts(mockConcerts);

    navigate("/concerts");
  };

  return (
    <>
      <div className="page-container">
        <TopBar />
        <div className="hero">
          <h1>Find events near you</h1>
          <h2>
            Browse more than <span>5,000</span> events near you
          </h2>
          <div className="search-container">
            <input
              type="text"
              placeholder="Search..."
              value={searchInput}
              onChange={(e) => setSearchInput(e.target.value)}
              disabled={isSearching}
            />
            <button onClick={handleSearch} disabled={isSearching}>
              {isSearching ? "Searching..." : "Search"}
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default HomePage;

// cerinte mpp
// operatii crud si filtrari
// unit testing pe sort or filter, assert
// toate entitatile sa fie salvate in memory

// silver challenge
// unit testing pentru tot
// statistica: top middle si bottom

// gold challenge
// paginare, cate iteme pe pagina, cate pagini, next prev
// chart pie
// pe un alt thread (faker) sa genereze date care sa schimbe in timp real datele din aplicatie
