import React, { useState } from "react";
import Concert from "../types/Concert";

type ConcertListProps = {
    concerts: Concert[];
    loading: boolean;
    setConcerts: (concerts: Concert[]) => void;
};

const ConcertList: React.FC<ConcertListProps> = ({concerts , loading, setConcerts}) => {
    
    const deleteConcert = (id: number) => {
        setConcerts(concerts.filter(concert => concert.id !== id));
    };

    
    return (
        <div className="p-5">
           
        
            {concerts.length === 0 && !loading && <p>No concerts found</p>}

            {loading ? <p>Loading concerts...</p> : (
                <ul>
                {concerts.map(concert => (
                    <li key={concert.id} className="p-3 border-b">
                        {concert.artist} - {concert.location} - {new Date(concert.startTime).toLocaleString()} 
                        <button onClick={() => deleteConcert(concert.id)} className="ml-2 p-1 bg-red-500 text-white rounded">Delete</button>
                    </li>
                ))}
            </ul>
            )}
        
        </div>
    );
};

export default ConcertList;