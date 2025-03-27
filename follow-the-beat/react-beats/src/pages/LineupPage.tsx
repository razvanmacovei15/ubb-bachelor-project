import React, { useState } from "react";
import { useLineup } from "../components/contexts/LineupContext";

const LineupPage: React.FC = () => {
  const { lineup, getSortedLineup, filterLineup, removeFromLineup, editConcert } = useLineup();

  const [sortBy, setSortBy] = useState<"artist" | "time" | "none">("none");
  const [searchTerm, setSearchTerm] = useState<string>("");

  // Store temporary text area input for each concert
  const [detailsDraft, setDetailsDraft] = useState<{ [id: number]: string }>({});

  const handleSort = () => {
    if (sortBy === "none") return lineup;
    return getSortedLineup(sortBy);
  };

  const handleFilter = (concerts: typeof lineup) => {
    return searchTerm ? filterLineup(searchTerm) : concerts;
  };

  const displayConcerts = handleFilter(handleSort());

  const handleUpdate = (id: number) => {
    if (detailsDraft[id] !== undefined) {
      editConcert(id, detailsDraft[id]);
    }
  };

  return (
    <div className="p-5">
      <h1 className="text-2xl mb-4">Your Lineup</h1>

      <div className="flex gap-4 mb-4 items-center">
        <input
          type="text"
          placeholder="Search by artist or location"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="border p-2 rounded w-64"
        />

        <select
          value={sortBy}
          onChange={(e) => setSortBy(e.target.value as "artist" | "time" | "none")}
          className="border p-2 rounded"
        >
          <option value="none">No Sorting</option>
          <option value="artist">Sort by Artist</option>
          <option value="time">Sort by Time</option>
        </select>
      </div>

      {displayConcerts.length === 0 ? (
        <p>No concerts found.</p>
      ) : (
        <ul className="space-y-4">
          {displayConcerts.map((concert) => (
            <li key={concert.id} className="border p-4 rounded shadow-sm">
              <div className="flex justify-between items-start gap-4">
                <div className="flex-1">
                  <p><strong>{concert.artist}</strong> - {concert.location}</p>
                  <p className="text-sm text-gray-600">{new Date(concert.startTime).toLocaleString()}</p>

                  <textarea
                    placeholder="Add details..."
                    value={detailsDraft[concert.id] ?? concert.details}
                    onChange={(e) =>
                      setDetailsDraft({ ...detailsDraft, [concert.id]: e.target.value })
                    }
                    className="mt-2 w-full p-2 border rounded"
                  />

                  <button
                    onClick={() => handleUpdate(concert.id)}
                    className="mt-2 p-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                  >
                    Update
                  </button>
                </div>

                <button
                  onClick={() => removeFromLineup(concert.id)}
                  className="p-2 bg-red-600 text-white rounded self-start"
                >
                  Remove
                </button>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default LineupPage;
