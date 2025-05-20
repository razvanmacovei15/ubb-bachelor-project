import { useUser } from "@/contexts/UserContext";
import { useConcertSortingFilteringContext } from "../../contexts/ConcertSortingFiltering";
import "./FestivalConcertFilter.css";
import { Button } from "../ui/button";
import { useState } from "react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "../ui/dropdown-menu";
import axios from "axios";

const FestivalConcertFilter = () => {
  const {
    festivalId,
    searchTerm,
    setSearchTerm,
    setSortBy,
    setItemsPerPage,
    setDate,
  } = useConcertSortingFilteringContext();
  const { isConnectedToSpotify } = useUser();
  const API_URL = import.meta.env.VITE_API_URL;
  const sessionToken = localStorage.getItem("sessionToken");
  const [generatingCompatibility, setGeneratingCompatibility] = useState(false);
  const generateCompatibility = async (
    festivalId: string,
    timeRange: "SHORT_TERM" | "MEDIUM_TERM" | "LONG_TERM" = "SHORT_TERM"
  ) => {
    setGeneratingCompatibility(true);
    try {
      const response = await axios.get(`${API_URL}/api/v1/user/suggestions`, {
        params: {
          range: timeRange,
          festivalId: festivalId,
        },
        headers: {
          Authorization: `Bearer ${sessionToken}`,
        },
      });
      console.log(response.data);
    } catch (error) {
      console.error(error);
    } finally {
      setGeneratingCompatibility(false);
    }
  };

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
            onChange={(e) =>
              setSortBy(e.target.value as "none" | "artist" | "time")
            }
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
      <div className="divider my-4 border-t border-gray-200"></div>
      {festivalId && isConnectedToSpotify && (
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button
              className="bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white font-semibold shadow-lg hover:shadow-xl transition-all w-full h-12 relative"
              disabled={generatingCompatibility}
            >
              {generatingCompatibility ? (
                <>
                  <span className="animate-spin inline-block mr-2 h-4 w-4 border-2 border-white border-t-transparent rounded-full" />
                  Generating compatibility...
                </>
              ) : (
                "Generate Compatibility"
              )}
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent className="w-56 bg-gray-800/80 backdrop-blur-sm p-1">
            <DropdownMenuItem
              onClick={() => generateCompatibility(festivalId, "SHORT_TERM")}
              className="bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white font-semibold cursor-pointer mb-1 last:mb-0 w-full"
            >
              Over past 4 weeks
            </DropdownMenuItem>
            <DropdownMenuItem
              onClick={() => generateCompatibility(festivalId, "MEDIUM_TERM")}
              className="bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white font-semibold cursor-pointer mb-1 last:mb-0 w-full"
            >
              Over past 6 months
            </DropdownMenuItem>
            <DropdownMenuItem
              onClick={() => generateCompatibility(festivalId, "LONG_TERM")}
              className="bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white font-semibold cursor-pointer mb-1 last:mb-0 w-full"
            >
              Over all time
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      )}
    </div>
  );
};

export default FestivalConcertFilter;
