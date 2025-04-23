import Concert from "../types/Concert";
import LineupConcert from "../types/LineupConcert";

export const addToLineup = (lineup: LineupConcert[], concert: Concert): LineupConcert[] => {
    if (lineup.some(c => c.id === concert.id)) return lineup;

    const newLineupConcert: LineupConcert = {
        ...concert,
        details: ""  
    };

    return [...lineup, newLineupConcert];
};
export const removeFromLineup = (lineup: LineupConcert[], id: number): LineupConcert[] => {
    return lineup.filter(c => c.id !== id);
};

export const editConcert = (lineup: LineupConcert[], id: number, details: string): LineupConcert[] => {
    return lineup.map(c => c.id === id ? { ...c, details } : c);
};

export const sortLineup = (lineup: LineupConcert[], by: "artist" | "time"): LineupConcert[] => {
    const copy = [...lineup];
    return copy.sort((a, b) =>
        by === "artist"
            ? a.artist.localeCompare(b.artist)
            : new Date(a.startTime).getTime() - new Date(b.startTime).getTime()
    );
};

export const filterLineup = (lineup: LineupConcert[], query: string): LineupConcert[] => {
    return lineup.filter(c =>
        c.artist.toLowerCase().includes(query.toLowerCase()) ||
        c.location.toLowerCase().includes(query.toLowerCase())
    );
};
