import { createContext, ReactNode, useContext, useState } from "react";
import Concert from "../types/Concert";
import LineupConcert from "../types/LineupConcert";
import {
  addToLineup as addFn,
  removeFromLineup as removeFn,
  editConcert as editFn,
  sortLineup,
  filterLineup,
} from "../../utils/lineupUtils";

interface LineupContextType {
  lineup: LineupConcert[];
  setLineup: (lineup: LineupConcert[]) => void;
  addToLineup: (concert: Concert) => void;
  removeFromLineup: (id: number) => void;
  editConcert: (id: number, details: string) => void;
  getSortedLineup: (by: "artist" | "time") => LineupConcert[];
  filterLineup: (query: string) => LineupConcert[];
}

const LineupContext = createContext<LineupContextType | undefined>(undefined);

export const LineupProvider = ({ children }: { children: ReactNode }) => {
  const [lineup, setLineup] = useState<LineupConcert[]>([]);

  const addToLineup = (concert: Concert) => {
    setLineup((prev) => addFn(prev, concert));
  };

  const removeFromLineup = (id: number) => {
    setLineup((prev) => removeFn(prev, id));
  };

  const editConcert = (id: number, details: string) => {
    setLineup((prev) => editFn(prev, id, details));
  };

  const getSortedLineup = (by: "artist" | "time") => {
    return sortLineup(lineup, by);
  };

  const filterLineupFn = (query: string) => {
    return filterLineup(lineup, query);
  };

  return (
    <LineupContext.Provider
      value={{
        lineup,
        setLineup,
        addToLineup,
        removeFromLineup,
        editConcert,
        getSortedLineup,
        filterLineup: filterLineupFn,
      }}
    >
      {children}
    </LineupContext.Provider>
  );
};

export const useLineup = () => {
  const context = useContext(LineupContext);
  if (!context)
    throw new Error("useLineup must be used within a LineupProvider");
  return context;
};
