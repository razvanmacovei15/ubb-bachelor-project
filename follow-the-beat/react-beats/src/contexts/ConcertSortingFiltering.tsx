import { createContext, useContext, useState, useEffect } from "react";
import { ConcertResponseDto } from "../types/ConcertResponseDto.ts";
import axios from "axios";
import { useUser } from "./UserContext";
import { FestivalUserDto } from "@/types/FestivalUserDto.ts";

type ConcertSortingFilteringContextType = {
  searchTerm: string;
  setSearchTerm: (term: string) => void;
  sortBy: "none" | "artist" | "compatibility";
  setSortBy: (sort: "none" | "artist" | "compatibility") => void;
  sortDirection: "asc" | "desc";
  setSortDirection: (dir: "asc" | "desc") => void;
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
  compatibilityTimeRange: string | null;
  setCompatibilityTimeRange: (range: string | null) => void;
};

const ConcertSortingFilteringContext = createContext<
  ConcertSortingFilteringContextType | undefined
>(undefined);

export const ConcertSortingFilteringProvider = ({
  children,
}: {
  children: React.ReactNode;
}) => {
  const { isConnectedToSpotify, sessionToken } = useUser();
  const [searchTerm, setSearchTerm] = useState("");
  const [sortBy, setSortBy] = useState<"none" | "artist" | "compatibility">(
    "none"
  );
  const [sortDirection, setSortDirection] = useState<"asc" | "desc">("asc");
  const [itemsPerPage, setItemsPerPage] = useState(15);
  const [currentPage, setCurrentPage] = useState(1);
  const [concerts, setConcerts] = useState<ConcertResponseDto[]>([]);
  const [totalCount, setTotalCount] = useState(0);
  const [festivalId, setFestivalId] = useState<string | null>(null);
  const [hasFestival, setHasFestival] = useState(false);
  const [compatibilityTimeRange, setCompatibilityTimeRange] = useState<
    string | null
  >(null);
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
          Authorization: `Bearer ${sessionToken}`,
        },
      });

      const festivalUser = response.data as FestivalUserDto;
      console.log("Has festival data:", response);
      const hasGeneratedCompatibility = festivalUser.generatedCompatibility;
      setHasFestival(hasGeneratedCompatibility);
      setCompatibilityTimeRange(festivalUser.timeRange);
      return hasGeneratedCompatibility;
    } catch (error) {
      console.error("Error checking hasFestival:", error);
      setHasFestival(false);
      return false;
    }
  };

  useEffect(() => {
    const updateHasFestival = async () => {
      if (festivalId && sessionToken) {
        const hasFestivalValue = await checkHasFestival();
        setHasFestival(hasFestivalValue);

        console.log(
          "[ConcertSortingFilteringContext] Has festival value:",
          hasFestivalValue
        );
        console.log(
          "[ConcertSortingFilteringContext] Compatibility time range:",
          compatibilityTimeRange
        );
      } else {
        setHasFestival(false);
        setCompatibilityTimeRange(null);
      }
    };
    updateHasFestival();
  }, [festivalId, sessionToken]);

  const fetchConcerts = async () => {
    if (festivalId) {
      await checkHasFestival();
    } else {
      setHasFestival(false);
      setCompatibilityTimeRange(null);
    }

    const params = {
      artist: searchTerm,
      page: currentPage - 1,
      size: itemsPerPage,
      sortBy: sortBy === "artist" ? "compatibility" : "createdAt",
      direction: sortDirection,
    };

    const endpoint = isConnectedToSpotify
      ? "/api/v2/concerts"
      : "/api/v1/concerts";
    const headers = isConnectedToSpotify
      ? { Authorization: `Bearer ${sessionToken}` }
      : {};

    const response = await axios.get(`${API_URL}${endpoint}`, {
      params,
      headers,
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
    try {
      const hasFestivalValue = await checkHasFestival();
      setHasFestival(hasFestivalValue);

      // Determine sorting logic
      let sortParam: string;
      if (sortBy === "artist") {
        sortParam = "concert.artist.name";
      } else if (isConnectedToSpotify && sortBy === "compatibility") {
        sortParam = "compatibility";
      } else {
        sortParam = "createdAt";
      }

      const params: Record<string, any> = {
        artist: searchTerm,
        page: currentPage - 1,
        size: itemsPerPage,
        sortBy: sortParam,
        direction: sortDirection,
        festivalId,
      };

      const endpoint = isConnectedToSpotify
        ? "/api/v2/concerts/festival"
        : "/api/v1/concerts/by-festival";

      const headers = isConnectedToSpotify
        ? { Authorization: `Bearer ${sessionToken}` }
        : {};

      const response = await axios.get(`${API_URL}${endpoint}`, {
        params,
        headers,
      });

      const data = response.data as {
        _embedded?: { concertResponseDtoList: ConcertResponseDto[] };
        page?: { totalElements: number };
      };

      console.log("Concerts data:", data);

      const concertsList = data._embedded?.concertResponseDtoList || [];
      setConcerts(concertsList);
      setTotalCount(data.page?.totalElements || 0);
    } catch (err) {
      console.error("Failed to fetch festival concerts:", err);
      // optionally show UI feedback
    }
  };

  const resetAndSelectFestival = (id: string | null) => {
    resetFilters();
    setFestivalId(id);
    if (!id) {
      setHasFestival(false);
      setCompatibilityTimeRange(null);
    }
  };

  const resetFilters = () => {
    setSearchTerm("");
    setSortBy("none");
    setCurrentPage(1);
    setSortDirection("asc");
    setItemsPerPage(15);
  };

  return (
    <ConcertSortingFilteringContext.Provider
      value={{
        searchTerm,
        setSearchTerm,
        sortBy,
        setSortBy,
        sortDirection,
        setSortDirection,
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
        compatibilityTimeRange,
        setCompatibilityTimeRange,
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
