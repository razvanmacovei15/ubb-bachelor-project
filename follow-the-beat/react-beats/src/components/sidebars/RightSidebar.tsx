import { useState } from "react";
import { useConcertSortingFilteringContext } from "../../contexts/ConcertSortingFiltering";
import "./RightSidebar.css";

const RightSidebar = () => {
  const { searchTerm, setSearchTerm, setSortBy, setItemsPerPage } =
    useConcertSortingFilteringContext();

  const [visibility, setVisibility] = useState(false);

  const toggleVisibility = () => {
    setVisibility(!visibility);
  };

  return (
    <div className="right-sidebar">
      <div className="rightmenu-container">
        <div className="flex flex-col items-center gap-4 mb-6">
          <input
            type="text"
            placeholder="Search..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="p-2 border rounded w-full text-white placeholder:text-gray-400"
          />
          <select
            onChange={(e) =>
              setSortBy(e.target.value as "none" | "artist" | "time")
            }
            className="p-2 w-full border rounded text-white"
          >
            <option value="none">No Sort</option>
            <option value="artist">Sort by Artist</option>
            <option value="time">Sort by Time</option>
          </select>
          <select
            onChange={(e) => setItemsPerPage(Number(e.target.value))}
            className="p-2 w-full border rounded text-white"
          >
            <option value={6}>6 per page</option>
            <option value={12}>12 per page</option>
            <option value={24}>24 per page</option>
          </select>
        </div>
      </div>
    </div>
  );
};

export default RightSidebar;
