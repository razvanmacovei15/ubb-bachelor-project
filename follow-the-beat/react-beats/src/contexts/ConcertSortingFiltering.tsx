import { createContext, useContext, useEffect, useState } from "react";
import {ConcertDto} from "../types/ConcertDto";

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
    concerts: ConcertDto[];
    totalCount: number;
    resetFilters: () => void;
};

const ConcertSortingFilteringContext = createContext<
    ConcertSortingFilteringContextType | undefined
>(undefined);

export const ConcertSortingFilteringProvider = ({
                                                    children,
                                                }: {
    children: React.ReactNode;
}) => {
    const [searchTerm, setSearchTerm] = useState("");
    const [sortBy, setSortBy] = useState<"none" | "artist" | "time">("none");
    const [date, setDate] = useState<string | null>(null);
    const [genres, setGenres] = useState<string[]>([]);
    const [itemsPerPage, setItemsPerPage] = useState(5);
    const [currentPage, setCurrentPage] = useState(1);

    const [concerts, setConcerts] = useState<ConcertDto[]>([]);
    const [totalCount, setTotalCount] = useState(0);

    const fetchConcerts = async () => {
        const params = new URLSearchParams({
            artist: searchTerm,
            page: (currentPage - 1).toString(),
            size: itemsPerPage.toString(),
            sortBy: sortBy === "artist" ? "artist.name" : "schedule.date",
            direction: "asc",
        });

        if (date) params.append("date", date);
        genres.forEach((genre) => params.append("genres", genre));

        const response = await fetch(
            `http://localhost:8080/api/v1/concerts?${params.toString()}`
        );
        const data = await response.json();

        const concertsList = data._embedded?.concertDTOList || [];
        setConcerts(concertsList);
        setTotalCount(data.page?.totalElements || 0);
    };

    useEffect(() => {
        fetchConcerts();
    }, [searchTerm, sortBy, date, genres, itemsPerPage, currentPage]);

    useEffect(() => {
        setCurrentPage(1);
    }, [searchTerm, sortBy, date, genres, itemsPerPage]);

    const resetFilters = () => {
        setSearchTerm("");
        setSortBy("none");
        setDate(null);
        setGenres([]);
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
