import React, { useState } from "react";

type Concert = {
    id: number;
    artist: string;
    location: string;
    startTime: string;
    compatibility: number;
};

type ConcertListProps = {
    addToLineup: (concert: Concert) => void;
};

type MyLineupProps = {
    lineup: Concert[];
};

const mockConcerts: Concert[] = Array.from({ length: 20 }, (_, i) => ({
    id: i + 1,
    artist: `Artist ${i + 1}`,
    location: `City ${i % 5}`,
    startTime: new Date(Date.now() + i * 3600000).toISOString(), // Future times
    compatibility: Math.floor(Math.random() * 101) // Random 0-100
}));

const ConcertList: React.FC<ConcertListProps> = ({ addToLineup }) => {
    const [concerts, setConcerts] = useState<Concert[]>(mockConcerts);
    const [searchTerm, setSearchTerm] = useState<string>("");
    const [sortBy, setSortBy] = useState<string | null>(null);
    const [itemsPerPage, setItemsPerPage] = useState<number>(5);
    const [currentPage, setCurrentPage] = useState<number>(1);

    const deleteConcert = (id: number) => {
        setConcerts(concerts.filter(concert => concert.id !== id));
    };

    const filteredConcerts = concerts.filter(concert => 
        concert.artist.toLowerCase().includes(searchTerm.toLowerCase()) ||
        concert.location.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const sortedConcerts = [...filteredConcerts].sort((a, b) => {
        if (sortBy === "time") return new Date(a.startTime).getTime() - new Date(b.startTime).getTime();
        if (sortBy === "alphabet") return a.artist.localeCompare(b.artist);
        return 0;
    });

    const totalPages = Math.ceil(sortedConcerts.length / itemsPerPage);
    const paginatedConcerts = sortedConcerts.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);

    return (
        <div className="p-5">
            <input
                type="text"
                placeholder="Search by artist/location..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="border p-2 rounded-full"
            />
            <button onClick={() => setSortBy("time")} className="ml-2 p-2 border rounded">Sort by Time</button>
            <button onClick={() => setSortBy("alphabet")} className="ml-2 p-2 border rounded">Sort A-Z</button>
            <select onChange={(e) => setItemsPerPage(Number(e.target.value))} className="ml-2 p-2 border rounded">
                <option value={5}>5</option>
                <option value={10}>10</option>
                <option value={20}>All</option>
            </select>
            <ul>
                {paginatedConcerts.map(concert => (
                    <li key={concert.id} className="p-3 border-b">
                        {concert.artist} - {concert.location} - {new Date(concert.startTime).toLocaleString()} 
                        <button onClick={() => addToLineup(concert)} className="ml-2 p-1 bg-green-500 text-white rounded">Add</button>
                        <button onClick={() => deleteConcert(concert.id)} className="ml-2 p-1 bg-red-500 text-white rounded">Delete</button>
                    </li>
                ))}
            </ul>
            <div className="flex justify-between mt-3">
                <button onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))} className="p-2 border rounded">Previous</button>
                <span>Page {currentPage} of {totalPages}</span>
                <button onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))} className="p-2 border rounded">Next</button>
            </div>
        </div>
    );
};

const MyLineup: React.FC<MyLineupProps> = ({ lineup }) => (
    <div className="p-5 border-t">
        <h2>My Lineup</h2>
        <ul>
            {lineup.map(concert => (
                <li key={concert.id}>{concert.artist} - {concert.location} - {new Date(concert.startTime).toLocaleString()}</li>
            ))}
        </ul>
    </div>
);

const HomePage: React.FC = () => {
    const [lineup, setLineup] = useState<Concert[]>([]);
    
    const addToLineup = (concert: Concert) => {
        if (!lineup.some(c => c.id === concert.id)) {
            setLineup([...lineup, concert]);
        }
    };

    return (
        <div className="flex flex-col w-full h-full items-center justify-center bg-gray-500">
            <h1 className="text-5xl w-full text-center p-5">Find events near you</h1>
            <h2 className="text-xl text-center p-5">Browse more than 10,000 events</h2>
            <ConcertList addToLineup={addToLineup} />
            <MyLineup lineup={lineup} />
        </div>
    );
};

export default HomePage;


// cerinte mpp
// operatii crud si filtrari
// unit testing pe sort or filter, assert 
// toate entitatile sa fie salvate in memory

// silver challenge
// unit testing pentru tot
// statistica: top middle si bottom 

// gold challenge
// paginare, cate iteme pe pagina, cate pagini, next prev
// chart pie 
// pe un alt thread (faker) sa genereze date care sa schimbe in timp real datele din aplicatie
