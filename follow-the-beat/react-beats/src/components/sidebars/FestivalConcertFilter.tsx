import { useConcertSortingFilteringContext } from "../../contexts/ConcertSortingFiltering";
import "./FestivalConcertFilter.css";

const FestivalConcertFilter = () => {
  const { searchTerm, setSearchTerm, setSortBy, setItemsPerPage, setDate } =
    useConcertSortingFilteringContext();

  return (
    <div className="right-sidebar">
      <div className="sidebar-header">
        <h2>Filters</h2>
      </div>
      <div className="sidebar-content">
        <div className="filter-section">
          <h3>Search</h3>
          <input
              type="text"
              placeholder="Search concerts..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-input"
          />
        </div>

        <div className="filter-section">
          <h3>Sort By</h3>
          <select
              onChange={(e) => setSortBy(e.target.value as "none" | "artist" | "time")}
              className="filter-select"
          >
            <option value="none">No Sort</option>
            <option value="artist">Artist Name</option>
            <option value="time">Date & Time</option>
          </select>
        </div>

        <div className="filter-section">
          <h3>Filter by Date</h3>
          <input
              type="date"
              className="search-input"
              onChange={(e) => setDate(e.target.value || null)}
          />
        </div>

        <div className="filter-section">
          <h3>Items Per Page</h3>
          <select
              onChange={(e) => setItemsPerPage(Number(e.target.value))}
              className="filter-select"
          >
            <option value={5}>5 items</option>
            <option value={15}>15 items</option>
            <option value={25}>25 items</option>
          </select>
        </div>
      </div>
    </div>
  );
};

export default FestivalConcertFilter;
