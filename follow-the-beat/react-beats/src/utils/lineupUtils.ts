import {ConcertDto} from "../types/ConcertDto.ts";
import LineupConcert from "../types/LineupConcert";

export const addToLineup = (lineup: LineupConcert[], concert: ConcertDto): LineupConcert[] => {
    if (lineup.some(c => c.id === concert.id)) return lineup;

    const newLineupConcert: LineupConcert = {
        ...concert,
        details: "",
        compatibility: 0,
    };

    return [...lineup, newLineupConcert];
};
export const removeFromLineup = (lineup: LineupConcert[], id: string): LineupConcert[] => {
    return lineup.filter(c => c.id !== id);
};

export const editConcert = (lineup: LineupConcert[], id: string, details: string): LineupConcert[] => {
    return lineup.map(c => c.id === id ? { ...c, details } : c);
};

export const sortLineup = (lineup: LineupConcert[], by: "artist" | "time"): LineupConcert[] => {
    const copy = [...lineup];
    return copy.sort((a, b) =>
        by === "artist"
            ? a.artistDTO.name.localeCompare(b.artistDTO.name)
            : new Date(a.scheduleDTO.startTime).getTime() - new Date(b.scheduleDTO.startTime).getTime()
    );
};

export const filterLineup = (lineup: LineupConcert[], query: string): LineupConcert[] => {
    return lineup.filter(c =>
        c.artistDTO.name.toLowerCase().includes(query.toLowerCase()) ||
        c.locationDTO.name.toLowerCase().includes(query.toLowerCase())
    );
};
