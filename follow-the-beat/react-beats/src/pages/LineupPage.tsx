import React, { useState } from "react";
import { useLineup } from "../components/contexts/LineupContext";
import TopBar from "../components/top-bar/TopBar";
import "./LineupPage.css";
import LineupConcertCard from "../components/concertspage/LineupConcertCard";

const LineupPage: React.FC = () => {
  const getGradientClass = (compatibility: number): string => {
    if (compatibility <= 33)
      return "bg-gradient-to-r from-[#cd7f32]/50 to-white";
    if (compatibility <= 66)
      return "bg-gradient-to-r from-gray-400/50 to-white";
    return "bg-gradient-to-r from-yellow-300/50 to-white";
  };

  const {
    lineup,
    getSortedLineup,
    filterLineup,
    removeFromLineup,
    editConcert,
  } = useLineup();

  const [sortBy, setSortBy] = useState<"artist" | "time" | "none">("none");
  const [searchTerm, setSearchTerm] = useState<string>("");

  const [detailsDraft, setDetailsDraft] = useState<{ [id: number]: string }>(
    {}
  );

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
    <div className="lineuppage-container">
      <TopBar />
      <h1 className="lineuppage-title">Your Lineup</h1>

      {displayConcerts.length === 0 ? (
        <p>No concerts found.</p>
      ) : (
        <div className="flex flex-col gap-8 w-full">
          {displayConcerts.map((concert) => (
            <LineupConcertCard
              key={concert.id}
              concert={concert}
              getGradientClass={getGradientClass}
              handleUpdate={function (id: number): void {
                throw new Error("Function not implemented.");
              }}
              removeFromLineup={function (id: number): void {
                throw new Error("Function not implemented.");
              }}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default LineupPage;
