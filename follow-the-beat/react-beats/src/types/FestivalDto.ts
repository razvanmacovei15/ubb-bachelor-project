import {ArtistDto} from "./ArtistDto";
import { StageDto } from "./StageDto";

export interface FestivalDto {
    id: string;
    name: string;
    description: string;
    location: string;
    startDate: string; // ISO date string
    endDate: string;   // ISO date string
    logoUrl: string;
    websiteUrl: string;
    isActive: boolean;
    dtoArtists: ArtistDto[];
    dtoStages: StageDto[];
    festivalImageUrl: string;
}
