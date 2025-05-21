export interface LineupEntryDto {
    id?: string;        // UUID as string
    concertId: string;    // UUID as string
    notes?: string;       // optional
    priority?: number;    // optional
}
