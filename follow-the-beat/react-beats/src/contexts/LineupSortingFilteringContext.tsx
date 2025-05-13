import {createContext, useContext, useEffect, useState} from "react";
import {LineupEntryDto} from "../types/LineupEntryDto.ts";
import axios from "axios";
import {LineupDetailDto} from "@/types/LineupDetailDto.ts";

type LineupSortingFilteringContextType = {
    searchTerm: string;
    setSearchTerm: (term: string) => void;
    sortBy: "none" | "priority" | "compatibility";
    setSortBy: (sort: "none" | "priority" | "compatibility") => void;
    itemsPerPage: number;
    setItemsPerPage: (count: number) => void;
    currentPage: number;
    setCurrentPage: (page: number) => void;
    lineupEntries: LineupDetailDto[];
    totalCount: number;
    resetFilters: () => void;
    userId: string | null;
    setUserId: (id: string | null) => void;
    addLineupEntry: (lineupEntryDto: LineupEntryDto) => Promise<void>;
    updateLineupEntry: (id: string, entry: Partial<LineupEntryDto>) => Promise<void>;
    removeLineupEntry: (id: string) => Promise<void>;
};

const LineupSortingFilteringContext = createContext<LineupSortingFilteringContextType | undefined>(undefined);

export const LineupSortingFilteringProvider = ({children}: { children: React.ReactNode }) => {
    const sessionToken = localStorage.getItem("sessionToken");

    const [searchTerm, setSearchTerm] = useState("");
    const [sortBy, setSortBy] = useState<"none" | "priority" | "compatibility">("none");
    const [itemsPerPage, setItemsPerPage] = useState(5);
    const [currentPage, setCurrentPage] = useState(1);
    const [lineupEntries, setLineupEntries] = useState<LineupDetailDto[]>([]);
    const [totalCount, setTotalCount] = useState(0);
    const [userId, setUserId] = useState<string | null>(null);

    const API_URL = import.meta.env.VITE_API_URL;

    const fetchLineupDetails = async () => {

        const newParams = {
            artist: searchTerm,
            page: currentPage - 1,
            size: itemsPerPage,
            sortBy: sortBy === "priority" ? "priority" : "compatibility",
            direction: "asc",

        }

        const response = await axios.get(`${API_URL}/api/lineup/search/details`, {
            params: newParams,
            headers: {
                Authorization: `Bearer ${sessionToken}`,
            },
        });
        const data = response.data as {
            _embedded?: { lineupDetailDtoList: LineupDetailDto[] };
            page?: { totalElements: number };
        };
        const entries = data._embedded?.lineupDetailDtoList || []; // Spring Boot returns a Page object with content
        setLineupEntries(entries);
        setTotalCount(data.page?.totalElements || 0);

    };

    const addLineupEntry = async (entry: LineupEntryDto) => {
        try {
            await axios.post(`${API_URL}/api/lineup`, entry, {
                headers: {Authorization: `Bearer ${sessionToken}`},
            });
            await fetchLineupDetails(); // Always refresh full details after a mutation
        } catch (err: any) {
            if (err.response?.status === 400) {
                console.error("Validation error:", err.response.data);
            } else if (err.response?.status === 409) {
                console.error("Conflict error:", err.response.data);
            } else {
                console.error("Unexpected error:", err);
            }
        }
    };

    const updateLineupEntry = async (id: string, entry: Partial<LineupEntryDto>) => {
        try {
            await axios.put(`${API_URL}/api/lineup/${id}`, entry, {
                headers: {Authorization: `Bearer ${sessionToken}`},
            });
            await fetchLineupDetails(); // Refresh details
        } catch (err: any) {
            if (err.response?.status === 404) {
                console.error("Entry not found:", err.response.data);
            } else if (err.response?.status === 400) {
                console.error("Validation error:", err.response.data);
            } else {
                console.error("Unexpected error:", err);
            }
        }
    };

    const removeLineupEntry = async (id: string) => {
        try {
            await axios.delete(`${API_URL}/api/lineup/${id}`);
            setLineupEntries((prev) => prev.filter((e) => e.id !== id));
            await fetchLineupDetails();
        } catch (err: any) {
            if (err.response?.status === 404) {
                console.error("Entry not found:", err.response.data);
            } else {
                console.error("Unexpected error:", err);
            }
        }
    };

    useEffect(() => {

        fetchLineupDetails();

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
                addLineupEntry,
                updateLineupEntry,
                removeLineupEntry,
            }}
        >
            {children}
        </LineupSortingFilteringContext.Provider>
    );
};

export const useLineupSortingFilteringContext = () => {
    const context = useContext(LineupSortingFilteringContext);
    if (context === undefined) {
        throw new Error("useLineupSortingFilteringContext must be used within a LineupSortingFilteringProvider");
    }
    return context;
};
