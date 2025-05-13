export interface LineupEntryDto {
    concertId: string;    // UUID as string
    notes?: string;       // optional
    priority?: number;    // optional
    compatibility?: number; // optional
}
