import {createContext, useContext, useState, useEffect} from "react";
import {ConcertResponseDto} from "../types/ConcertResponseDto.ts";
import axios from "axios";
import {useUser} from "./UserContext";

type ConcertSortingFilteringContextType = {
    searchTerm: string;
    setSearchTerm: (term: string) => void;
    sortBy: "none" | "artist" | "time";
    setSortBy: (sort: "none" | "artist" | "time") => void;
    date: string | null;
    setDate: (date: string | null) => void;
    itemsPerPage: number;
    setItemsPerPage: (count: number) => void;
    currentPage: number;
    setCurrentPage: (page: number) => void;
    concerts: ConcertResponseDto[];
    totalCount: number;
    resetFilters: () => void;
    festivalId: string | null;
    setFestivalId: (id: string | null) => void;
    resetAndSelectFestival: (id: string | null) => void;
    fetchConcerts: () => Promise<void>;
    fetchFestivalConcerts: (festivalId: string) => Promise<void>;
    hasFestival: boolean;
};

const ConcertSortingFilteringContext = createContext<
    ConcertSortingFilteringContextType | undefined
>(undefined);

export const ConcertSortingFilteringProvider = ({
                                                    children,
                                                }: {
    children: React.ReactNode;
}) => {
    const {isConnectedToSpotify, sessionToken} = useUser();
    const [searchTerm, setSearchTerm] = useState("");
    const [sortBy, setSortBy] = useState<"none" | "artist" | "time">("none");
    const [date, setDate] = useState<string | null>(null);
    const [itemsPerPage, setItemsPerPage] = useState(15);
    const [currentPage, setCurrentPage] = useState(1);
    const [concerts, setConcerts] = useState<ConcertResponseDto[]>([]);
    const [totalCount, setTotalCount] = useState(0);
    const [festivalId, setFestivalId] = useState<string | null>(null);
    const [hasFestival, setHasFestival] = useState(false);
    const API_URL = import.meta.env.VITE_API_URL;

    const checkHasFestival = async (): Promise<boolean> => {
        if (!festivalId || !sessionToken) {
            setHasFestival(false);
            return false;
        }

        try {
            const params = { festivalId };
            const response = await axios.get(`${API_URL}/api/user/hasFestival`, {
                params,
                headers: {
                    Authorization: `Bearer ${sessionToken}`
                }
            });

            const hasFestivalValue = response.data as boolean;
            console.log("Has festival data:", response);
            setHasFestival(hasFestivalValue);
            return hasFestivalValue;
        } catch (error) {
            console.error("Error checking hasFestival:", error);
            setHasFestival(false);
            return false;
        }
    };

    useEffect(() => {
        if (festivalId && sessionToken) {
            checkHasFestival();
        } else {
            setHasFestival(false);
        }
    }, [festivalId, sessionToken]);

    const fetchConcerts = async () => {
        if (festivalId) {
            await checkHasFestival();
        } else {
            setHasFestival(false);
        }

        const params = {
            artist: searchTerm,
            page: currentPage - 1,
            size: itemsPerPage,
            sortBy: sortBy === "artist" ? "compatibility" : "createdAt",
            direction: "asc",
            ...(date ? {date} : {}),
        };

        const endpoint = isConnectedToSpotify ? "/api/v2/concerts" : "/api/v1/concerts";
        const headers = isConnectedToSpotify ? {Authorization: `Bearer ${sessionToken}`} : {};

        const response = await axios.get(`${API_URL}${endpoint}`, {
            params,
            headers
        });
        const data = response.data as {
            _embedded?: { concertResponseDtoList: ConcertResponseDto[] };
            page?: { totalElements: number };
        };
        console.log("Concerts data:", response);
        const concertsList = data._embedded?.concertResponseDtoList || [];

        setConcerts(concertsList);
        setTotalCount(data.page?.totalElements || 0);
    };

    const fetchFestivalConcerts = async (festivalId: string) => {
        await checkHasFestival();

        const params = {
            artist: searchTerm,
            page: currentPage - 1,
            size: itemsPerPage,
            sortBy: sortBy === "artist" ? "compatibility" : "createdAt",
            direction: "asc",
            festivalId,
            ...(date ? {date} : {}),
        };

        const endpoint = isConnectedToSpotify ? "/api/v2/concerts/festival" : "/api/v1/concerts/by-festival";
        const headers = isConnectedToSpotify ? {Authorization: `Bearer ${sessionToken}`} : {};

        const response = await axios.get(`${API_URL}${endpoint}`, {
            params,
            headers
        });
        const data = response.data as {
            _embedded?: { concertResponseDtoList: ConcertResponseDto[] };
            page?: { totalElements: number };
        };
        console.log("Concerts data:", response);

        const concertsList = data._embedded?.concertResponseDtoList || [];
        setConcerts(concertsList);
        setTotalCount(data.page?.totalElements || 0);
    };

    const resetAndSelectFestival = (id: string | null) => {
        resetFilters();
        setFestivalId(id);
        if (!id) {
            setHasFestival(false);
        }
    };

    const resetFilters = () => {
        setSearchTerm("");
        setSortBy("none");
        setDate(null);
        setCurrentPage(1);
    };

    return (
        <ConcertSortingFilteringContext.Provider
            value={{
                searchTerm,
                setSearchTerm,
                sortBy,
                setSortBy,
                date,
                setDate,
                itemsPerPage,
                setItemsPerPage,
                currentPage,
                setCurrentPage,
                concerts,
                totalCount,
                resetFilters,
                festivalId,
                setFestivalId,
                resetAndSelectFestival,
                fetchConcerts,
                fetchFestivalConcerts,
                hasFestival,
            }}
        >
            {children}
        </ConcertSortingFilteringContext.Provider>
    );
};

export const useConcertSortingFilteringContext = () => {
    const context = useContext(ConcertSortingFilteringContext);
    if (context === undefined) {
        throw new Error(
            "useConcertSortingFilteringContext must be used within a ConcertSortingFilteringProvider"
        );
    }
    return context;
};
