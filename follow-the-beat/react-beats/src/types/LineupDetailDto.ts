// types/LineupDetailsDto.ts

export interface LineupDetailDto {
    id: string;                  // UUID as string
    artistName: string;
    artistImageUrl: string;
    spotifyUrl: string | null;   // Assuming this can be null
    notes: string | null;
    priority: number | null;
    compatibility: number | null;
    startTime: string | null;    // "HH:mm:ss" or similar format
    date: string | null;         // ISO date string, e.g. "2025-05-13"
    stageName: string | null;
    festivalName: string | null;
}
