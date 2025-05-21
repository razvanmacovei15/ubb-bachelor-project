import React from "react";
import "./LineupPage.css";
import Pagination from "../components/pagination/Pagination";
import {useLineupSortingFilteringContext} from "../contexts/LineupSortingFilteringContext";
import {Input} from "../components/ui/input";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "../components/ui/select";
import LineupEntryCard from "@/components/concertspage/LineupEntryCard.tsx";

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
                    <h1 className="lineup-title">My Lineup</h1>
                    <div className="filters-container"
                         style={{
                             width: '100%',
                             padding: '1rem',
                             display: 'flex',
                             gap: '1rem',
                             justifyContent: 'center'
                         }}>
                        <Input
                            type="text"
                            placeholder="Search entries..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            style={{maxWidth: '300px'}}
                        />
                        <Select value={sortBy}
                                onValueChange={(value: "none" | "compatibility") => setSortBy(value)}>
                            <SelectTrigger style={{width: '200px'}}>
                                <SelectValue placeholder="Sort by"/>
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="none">No Sorting</SelectItem>
                                <SelectItem value="priority">Priority</SelectItem>
                                <SelectItem value="compatibility">Compatibility</SelectItem>
                            </SelectContent>
                        </Select>
                        <Select value={itemsPerPage.toString()}
                                onValueChange={(value) => setItemsPerPage(Number(value))}>
                            <SelectTrigger style={{width: '150px'}}>
                                <SelectValue placeholder="Items per page"/>
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="5">5 per page</SelectItem>
                                <SelectItem value="10">10 per page</SelectItem>
                                <SelectItem value="20">20 per page</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>

                    <div className="flex flex-col gap-4 px-4 w-full">
                        {lineupEntries.length === 0 ? (
                            <h2 style={{color: 'white', textAlign: 'center', width: '100%'}}>
                                No entries in your lineup. Start adding some concerts!
                            </h2>
                        ) : (
                            lineupEntries.map((entry) => (
                                <LineupEntryCard key={entry.id} lineupDetail={entry}/>
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