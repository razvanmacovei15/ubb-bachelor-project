import { createContext, useContext, useEffect, useState } from 'react';
import Concert from '../types/Concert';
import mockConcerts from '../data/Concerts';

interface ConcertsAroundContextProps {
  concertsAround: Concert[];
  setConcertsAround: (concerts: Concert[]) => void;
}

const ConcertsAroundContext = createContext<ConcertsAroundContextProps | undefined>(undefined);

export const ConcertsAroundProvider = ({children} : {children: React.ReactNode}) => {
    const [concertsAround, setConcertsAround] = useState<Concert[]>([]);
    
    useEffect(() => {
        const loadAssets = async () => {
            setConcertsAround(mockConcerts);
        }

        loadAssets();

      }, []);
    return (
        <ConcertsAroundContext.Provider value={{concertsAround: concertsAround, setConcertsAround: setConcertsAround}}>
            {children}
        </ConcertsAroundContext.Provider>
    )
}

export const userConcertsAround = () => {
    const context = useContext(ConcertsAroundContext);
    if (!context) {
      throw new Error("useFilterMonth must be used within a FilterMonthProvider");
    }
    return context;
  };