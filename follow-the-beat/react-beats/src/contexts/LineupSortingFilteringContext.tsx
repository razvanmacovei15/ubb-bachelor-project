import { createContext, useContext, useEffect, useState } from "react";
import { LineupEntryDto } from "../types/LineupEntryDto";

type LineupSortingFilteringContextType = {
    searchTerm: string;
    setSearchTerm: (term: string) => void;
    sortBy: "none" | "priority" | "compatibility";
    setSortBy: (sort: "none" | "priority" | "compatibility") => void;
    itemsPerPage: number;
    setItemsPerPage: (count: number) => void;
    currentPage: number;
    setCurrentPage: (page: number) => void;
    lineupEntries: LineupEntryDto[];
    totalCount: number;
    resetFilters: () => void;
    userId: string | null;
    setUserId: (id: string | null) => void;
};

const LineupSortingFilteringContext = createContext<
    LineupSortingFilteringContextType | undefined
>(undefined);

export const LineupSortingFilteringProvider = ({
    children,
}: {
    children: React.ReactNode;
}) => {
    const [searchTerm, setSearchTerm] = useState("");
    const [sortBy, setSortBy] = useState<"none" | "priority" | "compatibility">("none");
    const [itemsPerPage, setItemsPerPage] = useState(5);
    const [currentPage, setCurrentPage] = useState(1);

    const [lineupEntries, setLineupEntries] = useState<LineupEntryDto[]>([]);
    const [totalCount, setTotalCount] = useState(0);

    const [userId, setUserId] = useState<string | null>(null);

    const fetchLineupEntries = async () => {
        const params = new URLSearchParams({
            userId: userId || "",
            page: (currentPage - 1).toString(),
            size: itemsPerPage.toString(),
            sortBy: sortBy === "priority" ? "hasPriority" : "hasCompatibility",
            direction: "asc",
        });

        if (searchTerm) params.append("search", searchTerm);

        const response = await fetch(
            `http://localhost:8080/api/lineup/search?${params.toString()}`
        );
        const data = await response.json();

        const lineupList = data.content || [];
        setLineupEntries(lineupList);
        setTotalCount(data.totalElements || 0);
    };

    useEffect(() => {
        if (userId) {
            fetchLineupEntries();
        }
    }, [searchTerm, sortBy, itemsPerPage, currentPage, userId]);

    useEffect(() => {
        setCurrentPage(1);
    }, [searchTerm, sortBy, itemsPerPage]);

    const resetFilters = () => {
        setSearchTerm("");
        setSortBy("none");
        setCurrentPage(1);
    };

    return (
        <LineupSortingFilteringContext.Provider
            value={{
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
                userId,
                setUserId,
            }}
        >
            {children}
        </LineupSortingFilteringContext.Provider>
    );
};

export const useLineupSortingFilteringContext = () => {
    const context = useContext(LineupSortingFilteringContext);
    if (context === undefined) {
        throw new Error(
            "useLineupSortingFilteringContext must be used within a LineupSortingFilteringProvider"
        );
    }
    return context;
};
