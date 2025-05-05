import { createContext, ReactNode, useContext, useState } from "react";
import ConcertDto from "../types/ConcertDto.ts";

interface ConcertContextType {
    concerts: ConcertDto[];
    setConcerts: (concerts: ConcertDto[]) => void;
}

const ConcertContext = createContext<ConcertContextType | undefined>(undefined);

export const ConcertProvider = ({children} : {children : ReactNode}) => {
    const [concerts, setConcerts] = useState<ConcertDto[]>([]);
    return (
        <ConcertContext.Provider value={{concerts, setConcerts}}>
            {children}
        </ConcertContext.Provider>
    );
}

export const useConcertContext = () => {
    const context = useContext(ConcertContext);
    if (context === undefined) {
        throw new Error("useConcert must be used within a ConcertProvider");
    }
    return context;
}