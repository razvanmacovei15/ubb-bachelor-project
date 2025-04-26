import React from "react";
import "./FilterSidebar.css";

interface FilterSidebarProps {
  timeRange: string;
  onTimeRangeChange: (range: string) => void;
  onSortChange: (sortBy: string) => void;
}

const FilterSidebar: React.FC<FilterSidebarProps> = ({
  timeRange,
  onTimeRangeChange,
  onSortChange,
}) => {
  return (
    <div className="filter-sidebar">
      <h3>Filters</h3>

      <div className="filter-section">
        <h4>Time Range</h4>
        <div className="filter-options">
          <button
            className={timeRange === "short_term" ? "active" : ""}
            onClick={() => onTimeRangeChange("short_term")}
          >
            Last 4 Weeks
          </button>
          <button
            className={timeRange === "medium_term" ? "active" : ""}
            onClick={() => onTimeRangeChange("medium_term")}
          >
            Last 6 Months
          </button>
          <button
            className={timeRange === "long_term" ? "active" : ""}
            onClick={() => onTimeRangeChange("long_term")}
          >
            All Time
          </button>
        </div>
      </div>

      <div className="filter-section">
        <h4>Sort By</h4>
        <div className="filter-options">
          <button onClick={() => onSortChange("popularity")}>Popularity</button>
          <button onClick={() => onSortChange("name")}>Name</button>
        </div>
      </div>
    </div>
  );
};

export default FilterSidebar;
