import React, { useEffect } from "react";
import "./LineupPage.css";
import Pagination from "../components/pagination/Pagination";
import { useLineupSortingFilteringContext } from "../contexts/LineupSortingFilteringContext";
import { Input } from "../components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../components/ui/select";
import LineupEntryCard from "@/components/concertspage/LineupEntryCard.tsx";
import { useUser } from "@/contexts/UserContext.tsx";

const LineupPage: React.FC = () => {
  const { isConnectedToSpotify, sessionToken } = useUser();

  const {
    searchTerm,
    setSearchTerm,
    sortBy,
    setSortBy,
    sortDirection,
    setSortDirection,
    itemsPerPage,
    setItemsPerPage,
    currentPage,
    setCurrentPage,
    lineupEntries,
    totalCount,
    fetchLineupDetails,
    setLineupEntries,
    setTotalCount,
    setLineupEntryIds,

    // resetFilters,
  } = useLineupSortingFilteringContext();

  useEffect(() => {
    if (isConnectedToSpotify) {
      fetchLineupDetails();
    } else {
      setLineupEntries([]);
      setTotalCount(0);
      setLineupEntryIds(new Set());
    }
  }, [
    searchTerm,
    sortBy,
    itemsPerPage,
    currentPage,
    isConnectedToSpotify,
    sortDirection,
  ]);

  useEffect(() => {
    setCurrentPage(1);
  }, [searchTerm, sortBy, itemsPerPage]);

  return (
    <div className="lineup-page-container">
      <div className="main-container">
        <div className="lineup-container">
          {/* <h1 className="lineup-title">My Lineup</h1> */}
          <div
            className="filters-container"
            style={{
              width: "100%",
              padding: "1rem",
              display: "flex",
              gap: "1rem",
              justifyContent: "center",
            }}
          >
            <Input
              type="text"
              placeholder="Search in lineup..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              style={{ maxWidth: "300px", color: "white" }}
              className="text-white placeholder:text-gray-400"
            />
            <Select
              value={sortBy}
              onValueChange={(value: "none" | "artist" | "compatibility") =>
                setSortBy(value)
              }
            >
              <SelectTrigger style={{ width: "200px" }} className="text-white">
                <SelectValue placeholder="Sort by" />
              </SelectTrigger>
              <SelectContent className="text-black">
                <SelectItem
                  value="none"
                  className="text-black hover:bg-gray-100 data-[state=checked]:bg-gray-300 data-[state=checked]:font-medium"
                >
                  No Sorting
                </SelectItem>
                <SelectItem
                  value="artist"
                  className="text-black hover:bg-gray-100 data-[state=checked]:bg-gray-300 data-[state=checked]:font-medium"
                >
                  Artist
                </SelectItem>
                <SelectItem
                  value="compatibility"
                  className="text-black hover:bg-gray-100 data-[state=checked]:bg-gray-300 data-[state=checked]:font-medium"
                >
                  Compatibility
                </SelectItem>
              </SelectContent>
            </Select>
            <Select
              value={sortDirection}
              onValueChange={(value: "asc" | "desc") => setSortDirection(value)}
            >
              <SelectTrigger style={{ width: "150px" }} className="text-white">
                <SelectValue placeholder="Sort order" />
              </SelectTrigger>
              <SelectContent className="text-black">
                <SelectItem
                  value="asc"
                  className="text-black hover:bg-gray-100 data-[state=checked]:bg-gray-300 data-[state=checked]:font-medium"
                >
                  Ascending
                </SelectItem>
                <SelectItem
                  value="desc"
                  className="text-black hover:bg-gray-100 data-[state=checked]:bg-gray-300 data-[state=checked]:font-medium"
                >
                  Descending
                </SelectItem>
              </SelectContent>
            </Select>
            <Select
              value={itemsPerPage.toString()}
              onValueChange={(value) => setItemsPerPage(Number(value))}
            >
              <SelectTrigger style={{ width: "150px" }} className="text-white">
                <SelectValue>{itemsPerPage} per page</SelectValue>
              </SelectTrigger>
              <SelectContent className="text-black">
                <SelectItem
                  value="5"
                  className="text-black hover:bg-gray-100 data-[state=checked]:bg-gray-300 data-[state=checked]:font-medium"
                >
                  5 per page
                </SelectItem>
                <SelectItem
                  value="10"
                  className="text-black hover:bg-gray-100 data-[state=checked]:bg-gray-300 data-[state=checked]:font-medium"
                >
                  10 per page
                </SelectItem>
                <SelectItem
                  value="15"
                  className="text-black hover:bg-gray-100 data-[state=checked]:bg-gray-300 data-[state=checked]:font-medium"
                >
                  15 per page
                </SelectItem>
                <SelectItem
                  value="20"
                  className="text-black hover:bg-gray-100 data-[state=checked]:bg-gray-300 data-[state=checked]:font-medium"
                >
                  20 per page
                </SelectItem>
              </SelectContent>
            </Select>
          </div>

          <div className="flex flex-col gap-4 px-4 w-full">
            {lineupEntries.length === 0 ? (
              <h2
                style={{ color: "white", textAlign: "center", width: "100%" }}
              >
                No entries in your lineup. Start adding some concerts!
              </h2>
            ) : (
              lineupEntries.map((entry) => (
                <LineupEntryCard key={entry.id} lineupDetail={entry} />
              ))
            )}
          </div>
          <Pagination
            className="pagination-bar"
            currentPage={currentPage}
            totalCount={totalCount}
            pageSize={itemsPerPage}
            onPageChange={(page) => setCurrentPage(page)}
          />
        </div>
      </div>
    </div>
  );
};

export default LineupPage;
