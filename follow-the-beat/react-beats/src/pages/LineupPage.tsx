import React, { useState } from "react";
import { useLineup } from "../components/contexts/LineupContext";

const LineupPage: React.FC = () => {
  const getGradientClass = (compatibility: number): string => {
    if (compatibility <= 33) return "bg-gradient-to-r from-[#cd7f32]/50 to-white";
    if (compatibility <= 66) return "bg-gradient-to-r from-gray-400/50 to-white";
    return "bg-gradient-to-r from-yellow-300/50 to-white";
  };
  
  const { lineup, getSortedLineup, filterLineup, removeFromLineup, editConcert } = useLineup();

  const [sortBy, setSortBy] = useState<"artist" | "time" | "none">("none");
  const [searchTerm, setSearchTerm] = useState<string>("");

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
        <ul className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {displayConcerts.map((concert) => (
            <li
              key={concert.id}
              className="w-full rounded-xl shadow overflow-hidden border h-60 flex"
            >
              <div
                className={`w-4/5 p-4 text-black flex flex-col justify-between ${getGradientClass(concert.compatibility)}`}
              >
                <div>
                  <p className="font-bold">{concert.artist}</p>
                  <p>{concert.location}</p>
                  <p className="text-sm text-gray-700">{new Date(concert.startTime).toLocaleString()}</p>
                </div>

                <textarea
                  placeholder="Add details..."
                  value={detailsDraft[concert.id] ?? concert.details}
                  onChange={(e) =>
                    setDetailsDraft({ ...detailsDraft, [concert.id]: e.target.value })
                  }
                  className="mt-2 w-full p-2 border rounded"
                />
              </div>

              <div className="w-1/5 flex flex-col items-center justify-end gap-2 p-2">
                <button
                  onClick={() => handleUpdate(concert.id)}
                  className="w-full p-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                >
                  Update
                </button>
                <button
                  onClick={() => removeFromLineup(concert.id)}
                  className="w-full p-2 bg-red-600 text-white rounded hover:bg-red-700"
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
