import {createContext, useContext, useEffect, useState} from "react";
import {LineupEntryDto} from "../types/LineupEntryDto.ts";
import axios from "axios";
import {LineupDetailDto} from "@/types/LineupDetailDto.ts";
import {useUser} from "./UserContext";

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
    updateLineupEntry: (
        id: string,
        entry: Partial<LineupEntryDto>
    ) => Promise<void>;
    removeLineupEntry: (id: string) => Promise<void>;
    isConcertInLineup: (concertId: string) => boolean;
};

const LineupSortingFilteringContext = createContext<
    LineupSortingFilteringContextType | undefined
>(undefined);

export const LineupSortingFilteringProvider = ({
                                                   children,
                                               }: {
    children: React.ReactNode;
}) => {
    const {isConnectedToSpotify, sessionToken} = useUser();

    const [searchTerm, setSearchTerm] = useState("");
    const [sortBy, setSortBy] = useState<"none" | "priority" | "compatibility">(
        "none"
    );
    const [itemsPerPage, setItemsPerPage] = useState(15);
    const [currentPage, setCurrentPage] = useState(1);
    const [lineupEntries, setLineupEntries] = useState<LineupDetailDto[]>([]);
    const [totalCount, setTotalCount] = useState(0);
    const [userId, setUserId] = useState<string | null>(null);
    const [lineupEntryIds, setLineupEntryIds] = useState<Set<string>>(new Set());

    const API_URL = import.meta.env.VITE_API_URL;

    const isConcertInLineup = (concertId: string): boolean => {
        return lineupEntryIds.has(concertId);
    };


    const fetchUserConcertsIds = async () => {
        try {
            const token = sessionToken || localStorage.getItem("sessionToken");

            const response = await axios.get<string[]>(`${API_URL}/api/lineup/concerts-ids`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            const ids = response.data;
            console.log("Fetched concert IDs:", ids);
            setLineupEntryIds(new Set(ids));
        } catch (err) {
            console.error("Failed to fetch concert IDs:", err);
        }
    };


    const fetchLineupDetails = async () => {
        if (!isConnectedToSpotify || !sessionToken) {
            setLineupEntries([]);
            setTotalCount(0);
            setLineupEntryIds(new Set());
            return;
        }

        const newParams = {
            artist: searchTerm,
            page: currentPage - 1,
            size: itemsPerPage,
            sortBy: sortBy === "priority" ? "priority" : "compatibility",
            direction: "asc",
        };

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
        const entries = data._embedded?.lineupDetailDtoList || [];
        setLineupEntries(entries);
        setTotalCount(data.page?.totalElements || 0);

        await fetchUserConcertsIds();
    };

    const addLineupEntry = async (entry: LineupEntryDto) => {
        if (!isConnectedToSpotify || !sessionToken) {
            console.warn("[LineupContext] Blocked addLineupEntry due to missing Spotify connection or sessionToken");
            return;
        }
        try {
            const res = await axios.post(`${API_URL}/api/lineup`, entry, {
                headers: { Authorization: `Bearer ${sessionToken}` },
            });
            await fetchLineupDetails();
            await fetchUserConcertsIds();
        } catch (err: any) {
            if (err.response?.status === 400) {
                console.error("[LineupContext] Validation error:", err.response.data);
            } else if (err.response?.status === 409) {
                console.error("[LineupContext] Conflict error:", err.response.data);
            } else {
                console.error("[LineupContext] Unexpected error:", err);
            }
        }
    };


    const updateLineupEntry = async (
        id: string,
        entry: Partial<LineupEntryDto>
    ) => {
        if (!isConnectedToSpotify || !sessionToken) return;

        try {
            await axios.put(`${API_URL}/api/lineup/${id}`, entry, {
                headers: {Authorization: `Bearer ${sessionToken}`},
            });
            await fetchLineupDetails();
            await fetchUserConcertsIds();
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
        if (!isConnectedToSpotify || !sessionToken) return;

        try {
            await axios.delete(`${API_URL}/api/lineup/${id}`, {
                headers: {Authorization: `Bearer ${sessionToken}`},
            });
            setLineupEntryIds((prev) => {
                const newSet = new Set(prev);
                newSet.delete(id);
                return newSet;
            });
            await fetchLineupDetails();
            await fetchUserConcertsIds();
        } catch (err: any) {
            if (err.response?.status === 404) {
                console.error("Entry not found:", err.response.data);
            } else {
                console.error("Unexpected error:", err);
            }
        }
    };

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
        userId,
        isConnectedToSpotify,
    ]);

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
                isConcertInLineup,
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
