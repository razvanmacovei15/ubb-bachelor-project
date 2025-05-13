import React from "react";
import "./LineupPage.css";
import Pagination from "../components/pagination/Pagination";
import { useLineupSortingFilteringContext } from "../contexts/LineupSortingFilteringContext";
import { Card, CardContent } from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select";

import { LineupEntryDto } from "../types/LineupEntryDto";

export const mockLineupEntries: LineupEntryDto[] = [
  {
    id: "1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p",
    userId: "user123",
    concertId: "concert456",
    notes: "Must see! Favorite artist",
    priority: 1,
    compatibility: 95
  },
  {
    id: "2b3c4d5e-6f7g-8h9i-0j1k-2l3m4n5o6p7q",
    userId: "user123",
    concertId: "concert789",
    notes: "Great venue, should be fun",
    priority: 2,
    compatibility: 85
  },
  {
    id: "3c4d5e6f-7g8h-9i0j-1k2l-3m4n5o6p7q8r",
    userId: "user123",
    concertId: "concert101",
    notes: "New artist, want to check them out",
    priority: 3,
    compatibility: 75
  },
  {
    id: "4d5e6f7g-8h9i-0j1k-2l3m-4n5o6p7q8r9s",
    userId: "user123",
    concertId: "concert202",
    notes: "Backup plan if other concerts are full",
    priority: 4,
    compatibility: 65
  },
  {
    id: "5e6f7g8h-9i0j-1k2l-3m4n-5o6p7q8r9s0t",
    userId: "user123",
    concertId: "concert303",
    notes: "Local band, supporting friends",
    priority: 5,
    compatibility: 55
  },
  {
    id: "6f7g8h9i-0j1k-2l3m-4n5o-6p7q8r9s0t1u",
    userId: "user123",
    concertId: "concert404",
    notes: "Classic rock night",
    priority: 6,
    compatibility: 45
  },
  {
    id: "7g8h9i0j-1k2l-3m4n-5o6p-7q8r9s0t1u2v",
    userId: "user123",
    concertId: "concert505",
    notes: "Late night show",
    priority: 7,
    compatibility: 35
  },
  {
    id: "8h9i0j1k-2l3m-4n5o-6p7q-8r9s0t1u2v3w",
    userId: "user123",
    concertId: "concert606",
    notes: "Acoustic session",
    priority: 8,
    compatibility: 25
  },
  {
    id: "9i0j1k2l-3m4n-5o6p-7q8r-9s0t1u2v3w4x",
    userId: "user123",
    concertId: "concert707",
    notes: "Jazz night",
    priority: 9,
    compatibility: 15
  },
  {
    id: "0j1k2l3m-4n5o-6p7q-8r9s-0t1u2v3w4x5y",
    userId: "user123",
    concertId: "concert808",
    notes: "Experimental music",
    priority: 10,
    compatibility: 5
  }
];

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
    // resetFilters,
  } = useLineupSortingFilteringContext();

  return (
    <div className="lineup-page-container">
      <div className="main-container">
        <div className="lineup-container">
          <h1>My Lineup</h1>
          <div className="filters-container" style={{ width: '100%', padding: '1rem', display: 'flex', gap: '1rem', justifyContent: 'center' }}>
            <Input
              type="text"
              placeholder="Search entries..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              style={{ maxWidth: '300px' }}
            />
            <Select value={sortBy} onValueChange={(value: "none" | "priority" | "compatibility") => setSortBy(value)}>
              <SelectTrigger style={{ width: '200px' }}>
                <SelectValue placeholder="Sort by" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="none">No Sorting</SelectItem>
                <SelectItem value="priority">Priority</SelectItem>
                <SelectItem value="compatibility">Compatibility</SelectItem>
              </SelectContent>
            </Select>
            <Select value={itemsPerPage.toString()} onValueChange={(value) => setItemsPerPage(Number(value))}>
              <SelectTrigger style={{ width: '150px' }}>
                <SelectValue placeholder="Items per page" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="5">5 per page</SelectItem>
                <SelectItem value="10">10 per page</SelectItem>
                <SelectItem value="20">20 per page</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {lineupEntries.length === 0 ? (
            <h2 style={{ color: 'white', textAlign: 'center', width: '100%' }}>
              No entries in your lineup. Start adding some concerts!
            </h2>
          ) : (
            <>
              <div className="lineup-grid">
                {lineupEntries.map((entry) => (
                  <Card key={entry.id} className="hover:shadow-lg transition-shadow">
                    <CardContent className="p-4">
                      <h3 className="text-lg font-semibold mb-2">Entry #{entry.id.slice(0, 8)}</h3>
                      <div className="space-y-2">
                        <p><strong>Priority:</strong> {entry.priority || 'Not set'}</p>
                        <p><strong>Compatibility:</strong> {entry.compatibility ? `${entry.compatibility}%` : 'Not calculated'}</p>
                        {entry.notes && (
                          <p><strong>Notes:</strong> {entry.notes}</p>
                        )}
                      </div>
                    </CardContent>
                  </Card>
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