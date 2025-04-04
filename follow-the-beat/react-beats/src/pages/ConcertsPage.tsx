import React, { useState } from "react";
import { useConcertContext } from "../components/contexts/ConcertContext";
import { useLineup } from "../components/contexts/LineupContext";

const ConcertsPage: React.FC = () => {
  const { concerts } = useConcertContext();
  const { lineup, addToLineup } = useLineup();

  const [searchTerm, setSearchTerm] = useState("");
  const [sortBy, setSortBy] = useState<"none" | "artist" | "time">("none");
  const [itemsPerPage, setItemsPerPage] = useState(5);
  const [currentPage, setCurrentPage] = useState(1);

  const filteredConcerts = concerts.filter((concert) =>
    concert.artist.toLowerCase().includes(searchTerm.toLowerCase()) ||
    concert.location.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const sortedConcerts = [...filteredConcerts].sort((a, b) => {
    if (sortBy === "artist") return a.artist.localeCompare(b.artist);
    if (sortBy === "time") return new Date(a.startTime).getTime() - new Date(b.startTime).getTime();
    return 0;
  });

  const totalPages = Math.ceil(sortedConcerts.length / itemsPerPage);
  const paginatedConcerts = sortedConcerts.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);

  return (
    <div className="p-6 max-w-4xl mx-auto">
      <h1 className="text-3xl font-semibold mb-6 text-center">Concerts Near You</h1>

      <div className="flex flex-wrap items-center gap-4 mb-6">
        <input
          type="text"
          placeholder="Search by artist or location..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="p-2 border rounded w-64"
        />
        <select
          onChange={(e) => setSortBy(e.target.value as "none" | "artist" | "time")}
          className="p-2 border rounded"
        >
          <option value="none">No Sort</option>
          <option value="artist">Sort by Artist</option>
          <option value="time">Sort by Time</option>
        </select>
        <select
          onChange={(e) => setItemsPerPage(Number(e.target.value))}
          className="p-2 border rounded"
        >
          <option value={5}>5 per page</option>
          <option value={10}>10 per page</option>
          <option value={20}>20 per page</option>
        </select>
      </div>

      <ul className="space-y-4">
        {paginatedConcerts.map((concert) => {
          const isInLineup = lineup.some((item) => item.id === concert.id);

          return (
            <li
              key={concert.id}
              className={`p-4 rounded flex justify-between items-center shadow ${
                isInLineup ? "border-purple-600 border-2" : "border"
              }`}
            >
              <div>
                <p><strong>{concert.artist}</strong> - {concert.location}</p>
                <p className="text-sm text-gray-600">{new Date(concert.startTime).toLocaleString()}</p>
              </div>
              <button
                onClick={() => addToLineup(concert)}
                className="p-2 px-4 bg-green-600 text-white rounded hover:bg-green-700"
              >
                Add
              </button>
            </li>
          );
        })}
      </ul>

      <div className="flex justify-between items-center mt-6">
        <button
          onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
          className="px-4 py-2 border rounded"
          disabled={currentPage === 1}
        >
          Previous
        </button>
        <span className="text-sm">Page {currentPage} of {totalPages}</span>
        <button
          onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
          className="px-4 py-2 border rounded"
          disabled={currentPage === totalPages}
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default ConcertsPage;
