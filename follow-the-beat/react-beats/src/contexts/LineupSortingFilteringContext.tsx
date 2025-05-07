import { createContext, useContext, useEffect, useState } from "react";
import { LineupEntryDto } from "../types/LineupEntryDto";
import { mockLineupEntries } from "../mocks/mockLineupEntries";

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
    addLineupEntry: (entry: Omit<LineupEntryDto, "id">) => Promise<void>;
    updateLineupEntry: (id: string, entry: Partial<LineupEntryDto>) => Promise<void>;
    removeLineupEntry: (id: string) => Promise<void>;
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
        // In development, use mock data
        if (process.env.NODE_ENV === 'development') {
            let filteredEntries = [...mockLineupEntries];
            
            // Apply search filter
            if (searchTerm) {
                filteredEntries = filteredEntries.filter(entry => 
                    entry.notes?.toLowerCase().includes(searchTerm.toLowerCase())
                );
            }

            // Apply sorting
            if (sortBy === "priority") {
                filteredEntries.sort((a, b) => (a.priority || 0) - (b.priority || 0));
            } else if (sortBy === "compatibility") {
                filteredEntries.sort((a, b) => (a.compatibility || 0) - (b.compatibility || 0));
            }

            // Apply pagination
            const startIndex = (currentPage - 1) * itemsPerPage;
            const endIndex = startIndex + itemsPerPage;
            const paginatedEntries = filteredEntries.slice(startIndex, endIndex);

            setLineupEntries(paginatedEntries);
            setTotalCount(filteredEntries.length);
            return;
        }

        // In production, fetch from API
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

    const addLineupEntry = async (entry: Omit<LineupEntryDto, "id">) => {
        try {
            if (process.env.NODE_ENV === 'development') {
                const newEntry: LineupEntryDto = {
                    ...entry,
                    id: Math.random().toString(36).substring(7),
                };
                setLineupEntries(prev => [...prev, newEntry]);
                setTotalCount(prev => prev + 1);
                return;
            }

            const response = await fetch("http://localhost:8080/api/lineup", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(entry),
            });

            if (!response.ok) {
                const error = await response.text();
                throw new Error(error);
            }

            await fetchLineupEntries();
        } catch (error) {
            console.error("Failed to add lineup entry:", error);
            throw error;
        }
    };

    const updateLineupEntry = async (id: string, entry: Partial<LineupEntryDto>) => {
        try {
            if (process.env.NODE_ENV === 'development') {
                setLineupEntries(prev => 
                    prev.map(item => 
                        item.id === id ? { ...item, ...entry } : item
                    )
                );
                return;
            }

            const response = await fetch(`http://localhost:8080/api/lineup/${id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(entry),
            });

            if (!response.ok) {
                const error = await response.text();
                throw new Error(error);
            }

            await fetchLineupEntries();
        } catch (error) {
            console.error("Failed to update lineup entry:", error);
            throw error;
        }
    };

    const removeLineupEntry = async (id: string) => {
        try {
            if (process.env.NODE_ENV === 'development') {
                setLineupEntries(prev => prev.filter(item => item.id !== id));
                setTotalCount(prev => prev - 1);
                return;
            }

            const response = await fetch(`http://localhost:8080/api/lineup/${id}`, {
                method: "DELETE",
            });

            if (!response.ok) {
                const error = await response.text();
                throw new Error(error);
            }

            await fetchLineupEntries();
        } catch (error) {
            console.error("Failed to remove lineup entry:", error);
            throw error;
        }
    };

    useEffect(() => {
        if (userId || process.env.NODE_ENV === 'development') {
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
        throw new Error(
            "useLineupSortingFilteringContext must be used within a LineupSortingFilteringProvider"
        );
    }
    return context;
};
