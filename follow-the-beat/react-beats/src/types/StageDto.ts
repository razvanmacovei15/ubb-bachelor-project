import { LocationDto } from "./LocationDTO";
import ConcertDto from "./ConcertDTO";
import {FestivalDto} from "./FestivalDTO";

export interface StageDto extends LocationDto {
    id: string;
    name: string;
    festivalDTO: FestivalDto;
    dtoConcerts: ConcertDto[];
}
