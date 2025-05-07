import React from "react";
import "./LineupPage.css";
import Pagination from "../components/pagination/Pagination";
import { useLineupSortingFilteringContext } from "../contexts/LineupSortingFilteringContext";

const LineupPage: React.FC = () => {
  const {
    searchTerm,
    setSearchTerm,
    sortBy,
    setSortBy,
    itemsPerPage,
    setItemsPerPage,
    currentPage,
    setCurrentPage,
    lineupEntries,
    totalCount,
    resetFilters,
  } = useLineupSortingFilteringContext();

  // Calculate statistics
  const totalPriority = lineupEntries.reduce((sum, entry) => sum + (entry.priority || 0), 0);
  const avgCompatibility = lineupEntries.length > 0
    ? lineupEntries.reduce((sum, entry) => sum + (entry.compatibility || 0), 0) / lineupEntries.length
    : 0;

  return (
    <div className="lineup-page-container">
      <div className="main-container">
        <div className="lineup-container">
          <h1>My Lineup</h1>

          <div className="lineup-stats">
            <div className="stats-grid">
              <div className="stat-card">
                <h3>Total Entries</h3>
                <p>{totalCount}</p>
              </div>
              <div className="stat-card">
                <h3>Total Priority</h3>
                <p>{totalPriority}</p>
              </div>
              <div className="stat-card">
                <h3>Avg. Compatibility</h3>
                <p>{avgCompatibility.toFixed(1)}%</p>
              </div>
            </div>
          </div>

          <div className="filters-container">
            <input
              type="text"
              placeholder="Search entries..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-input"
            />
            <select 
              value={sortBy} 
              onChange={(e) => setSortBy(e.target.value as "none" | "priority" | "compatibility")}
              className="sort-select"
            >
              <option value="none">No Sorting</option>
              <option value="priority">Priority</option>
              <option value="compatibility">Compatibility</option>
            </select>
            <select 
              value={itemsPerPage} 
              onChange={(e) => setItemsPerPage(Number(e.target.value))}
              className="items-select"
            >
              <option value="5">5 per page</option>
              <option value="10">10 per page</option>
              <option value="20">20 per page</option>
            </select>
          </div>

          {lineupEntries.length === 0 ? (
            <h2 className="no-entries-message">
              No entries in your lineup. Start adding some concerts!
            </h2>
          ) : (
            <>
              <div className="lineup-grid">
                {lineupEntries.map((entry) => (
                  <div key={entry.id} className="lineup-card">
                    <div className="card-content">
                      <h3 className="entry-title">Entry #{entry.id.slice(0, 8)}</h3>
                      <div className="entry-details">
                        <p><strong>Priority:</strong> {entry.priority || 'Not set'}</p>
                        <p><strong>Compatibility:</strong> {entry.compatibility ? `${entry.compatibility}%` : 'Not calculated'}</p>
                        {entry.notes && (
                          <p><strong>Notes:</strong> {entry.notes}</p>
                        )}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
              <Pagination
                className="pagination-bar"
                currentPage={currentPage}
                totalCount={totalCount}
                pageSize={itemsPerPage}
                onPageChange={(page) => setCurrentPage(page)}
              />
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default LineupPage; 