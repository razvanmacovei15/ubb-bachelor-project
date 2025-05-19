import { LocationDto } from "./LocationDto";
import {ConcertResp} from "./ConcertResponseDto.ts";
import {FestivalDto} from "./FestivalDto";

export interface StageDto extends LocationDto {
    id: string;
    name: string;
    festivalDTO: FestivalDto;
    dtoConcerts: ConcertResp[];
}
