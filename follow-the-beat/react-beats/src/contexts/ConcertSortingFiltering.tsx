import { createContext, useContext, useState } from "react";

interface ConcertSortingFilteringContextType {
  searchTerm: string;
  setSearchTerm: (searchTerm: string) => void;
  sortBy: string;
  setSortBy: (value: "none" | "artist" | "time") => void;
  itemsPerPage: number;
  setItemsPerPage: (setItemsPerPage: number) => void;
  resetFilters: () => void;
}

const ConcertSortingFilteringContext = createContext<
  ConcertSortingFilteringContextType | undefined
>(undefined);

export const ConcertSortingFilteringProvider = ({
  children,
}: {
  children: React.ReactNode;
}) => {
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [sortBy, setSortBy] = useState<"none" | "artist" | "time">("none");
  const [itemsPerPage, setItemsPerPage] = useState(6);

  const resetFilters = () => {
    setSearchTerm("");
    setSortBy("none");
  };

  return (
    <ConcertSortingFilteringContext.Provider
      value={{
        searchTerm,
        setSearchTerm,
        sortBy,
        setSortBy,
        itemsPerPage,
        setItemsPerPage,
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
