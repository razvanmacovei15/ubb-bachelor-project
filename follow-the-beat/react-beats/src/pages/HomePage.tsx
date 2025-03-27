import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useConcertContext } from "../components/contexts/ConcertContext";

const HomePage: React.FC = () => {
  const [isSearching, setIsSearching] = useState(false);
  const { setConcerts } = useConcertContext();
  const navigate = useNavigate();

  const handleSearch = async () => {
    setIsSearching(true);

    await new Promise(res => setTimeout(res, 3000));

    const mockConcerts = Array.from({ length: 20 }, (_, i) => ({
      id: i + 1,
      artist: `Artist ${i + 1}`,
      location: `City ${i % 5}`,
      startTime: new Date(Date.now() + i * 3600000).toISOString(),
      compatibility: Math.floor(Math.random() * 101),
    }));

    setConcerts(mockConcerts);

    navigate("/concerts");
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-500 text-white">
      <h1 className="text-5xl text-center p-5">Find events near you</h1>
      <h2 className="text-xl text-center p-5">Browse more than 10,000 events</h2>

      <input
        type="text"
        placeholder="Search..."
        className="p-3 rounded text-black"
        disabled={isSearching}
      />
      <button
        onClick={handleSearch}
        disabled={isSearching}
        className={`mt-4 p-3 rounded bg-blue-600 hover:bg-blue-700 transition ${
          isSearching ? "opacity-50 cursor-not-allowed" : ""
        }`}
      >
        {isSearching ? "Searching..." : "Search"}
      </button>

      {isSearching && <p className="mt-4 text-lg">Searching for concerts...</p>}
    </div>
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