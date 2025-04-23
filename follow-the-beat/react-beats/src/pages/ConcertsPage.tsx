import React, { useEffect, useMemo, useState } from "react";
import { useConcertContext } from "../contexts/ConcertContext";
import { useLineup } from "../contexts/LineupContext";
import TopBar from "../components/top-bar/TopBar";
import ConcertCard from "../components/concertspage/ConcertCard";
import "./ConcertsPage.css";
import Pagination from "../components/Pagination";
import { useConcertSortingFilteringContext } from "../contexts/ConcertSortingFiltering";
import RightSidebar from "../components/sidebars/RightSidebar";

const ConcertsPage: React.FC = () => {
  const { concerts } = useConcertContext();
  const { searchTerm, sortBy, itemsPerPage } =
    useConcertSortingFilteringContext();
  const { lineup, addToLineup, removeFromLineup } = useLineup();

  const [currentPage, setCurrentPage] = useState(1);

  const filteredConcerts = concerts.filter(
    (concert) =>
      concert.artist.toLowerCase().includes(searchTerm.toLowerCase()) ||
      concert.location.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const sortedConcerts = [...filteredConcerts].sort((a, b) => {
    if (sortBy === "artist") return a.artist.localeCompare(b.artist);
    if (sortBy === "time")
      return new Date(a.startTime).getTime() - new Date(b.startTime).getTime();
    return 0;
  });

  const currentTableData = useMemo(() => {
    const firstPageIndex = (currentPage - 1) * itemsPerPage;
    const lastPageIndex = firstPageIndex + itemsPerPage;
    return sortedConcerts.slice(firstPageIndex, lastPageIndex);
  }, [currentPage, itemsPerPage, sortedConcerts]);

  useEffect(() => {
    setCurrentPage(1);
  }, [itemsPerPage]);

  return (
    <div className="concertpage-container">
      <TopBar />
      <div className="main-container">
        <div className="concerts-container">
          {concerts.length === 0 ? (
            <h1>No concerts available. Please check back later.</h1>
          ) : (
            <h1>Concerts in {concerts[0].location}</h1>
          )}
          <div className="concert-grid">
            {currentTableData.map((concert) => {
              const isInLineup = lineup.some((item) => item.id === concert.id);
              return (
                <ConcertCard
                  key={concert.id}
                  concert={concert}
                  isInLineup={isInLineup}
                  onAdd={() => addToLineup(concert)}
                  onRemove={() => removeFromLineup(concert.id)}
                />
              );
            })}
          </div>
          <Pagination
            className="pagination-bar"
            currentPage={currentPage}
            totalCount={sortedConcerts.length}
            pageSize={itemsPerPage}
            onPageChange={(page) => setCurrentPage(page)}
          />
        </div>
        <RightSidebar />
      </div>
    </div>
  );
};

export default ConcertsPage;
