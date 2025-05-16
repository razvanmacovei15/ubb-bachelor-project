import { LocationDto } from "./LocationDto";
import {ConcertDto} from "./ConcertDto";
import {FestivalDto} from "./FestivalDto";

export interface StageDto extends LocationDto {
    id: string;
    name: string;
    festivalDTO: FestivalDto;
    dtoConcerts: ConcertDto[];
}
