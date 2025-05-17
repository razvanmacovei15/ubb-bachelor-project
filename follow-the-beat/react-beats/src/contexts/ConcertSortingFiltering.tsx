import { createContext, useContext, useEffect, useState } from "react";
import { ConcertDto } from "../types/ConcertDto";
import axios from "axios";

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
  festivalId: string | null;
  setFestivalId: (id: string | null) => void;
  resetAndSelectFestival: (id: string | null) => void;
  fetchConcerts: () => Promise<void>;
  fetchFestivalConcerts: (festivalId: string) => Promise<void>;
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
  const [itemsPerPage, setItemsPerPage] = useState(5);
  const [currentPage, setCurrentPage] = useState(1);
  const [concerts, setConcerts] = useState<ConcertDto[]>([]);
  const [totalCount, setTotalCount] = useState(0);
  const [festivalId, setFestivalId] = useState<string | null>(null);

  const API_URL = import.meta.env.VITE_API_URL;

  const fetchConcerts = async () => {
    const params = {
      artist: searchTerm,
      page: currentPage - 1,
      size: itemsPerPage,
      sortBy: sortBy === "artist" ? "artist.name" : "schedule.date",
      direction: "asc",
      ...(date ? { date } : {}),
    };

    const response = await axios.get(`${API_URL}/api/v1/concerts`, { params });
    const data = response.data as {
      _embedded?: { concertDTOList: ConcertDto[] };
      page?: { totalElements: number };
    };

    const concertsList = data._embedded?.concertDTOList || [];
    setConcerts(concertsList);
    setTotalCount(data.page?.totalElements || 0);
  };

  const fetchFestivalConcerts = async (festivalId: string) => {
    const params = {
      artist: searchTerm,
      page: currentPage - 1,
      size: itemsPerPage,
      sortBy: sortBy === "artist" ? "artist.name" : "schedule.date",
      direction: "asc",
      festivalId,
      ...(date ? { date } : {}),
    };

    const response = await axios.get(`${API_URL}/api/v1/concerts/by-festival`, {
      params,
    });
    const data = response.data as {
      _embedded?: { concertDTOList: ConcertDto[] };
      page?: { totalElements: number };
    };

    const concertsList = data._embedded?.concertDTOList || [];
    setConcerts(concertsList);
    setTotalCount(data.page?.totalElements || 0);
  };

  const resetAndSelectFestival = (id: string | null) => {
    resetFilters();
    setFestivalId(id);
  };

  // useEffect(() => {
  //   if (festivalId) {
  //     fetchFestivalConcerts(festivalId);
  //   } else {
  //     fetchConcerts();
  //   }
  // }, [searchTerm, sortBy, date, itemsPerPage, currentPage, festivalId]);
  //
  // useEffect(() => {
  //   setCurrentPage(1);
  // }, [searchTerm, sortBy, date, itemsPerPage]);

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
