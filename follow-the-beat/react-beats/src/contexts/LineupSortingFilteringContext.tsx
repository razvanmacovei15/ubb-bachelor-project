import {createContext, useContext, useEffect, useState} from "react";
import {LineupEntryDto} from "../types/LineupEntryDto";
import axios from "axios";

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
    const [lineupEntries, setLineupEntries] = useState<LineupEntryDto[]>([]);
    const [totalCount, setTotalCount] = useState(0);
    const [userId, setUserId] = useState<string | null>(null);

    const API_URL = import.meta.env.VITE_API_URL;

    const fetchLineupEntries = async () => {
        const params = new URLSearchParams();
        params.append("page", (currentPage - 1).toString());
        params.append("size", itemsPerPage.toString());
        params.append("sortBy",
            sortBy === "priority"
                ? "priority"
                : sortBy === "compatibility"
                    ? "compatibility"
                    : "addedAt"
        );

        params.append("direction", "asc");

        if (sortBy === "priority") {
            params.append("hasPriority", "1");
            params.append("hasPriorityGreaterThan", "0");
            params.append("minPriority", "0");
        }

        if (sortBy === "compatibility") {
            params.append("hasCompatibilityGreaterThan", "0");
            params.append("minCompatibility", "0");
        }
        const response = await axios.get(`${API_URL}/api/lineup/search`, {
            params: params,
            headers: {
                Authorization: `Bearer ${sessionToken}`,
            }
        });
        const data = response.data;
        const entries = data._embedded?.lineupEntryDTOList || [];
        setLineupEntries(entries);
        setTotalCount(data.page?.totalElements || 0);
    };

    const addLineupEntry = async (entry: LineupEntryDto) => {
        try {
            const response = await axios.post(`${API_URL}/api/lineup`, entry, {
                headers: {
                    Authorization: `Bearer ${sessionToken}`,
                },
            });
            console.log("Session token:", sessionToken);
            if (response.status === 201) {
                const newEntry = response.data;
                setLineupEntries((prev) => [...prev, newEntry]);
                await fetchLineupEntries();
            }
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
            const response = await axios.put(`${API_URL}/api/lineup/${id}`, entry);
            const updated = response.data;
            setLineupEntries((prev) =>
                prev.map((e) => (e.id === id ? updated : e))
            );
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
            await fetchLineupEntries();
        } catch (err: any) {
            if (err.response?.status === 404) {
                console.error("Entry not found:", err.response.data);
            } else {
                console.error("Unexpected error:", err);
            }
        }
    };

    useEffect(() => {
        if (userId || import.meta.env.MODE === "development") {
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
