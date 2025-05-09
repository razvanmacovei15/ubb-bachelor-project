import {LocationDto} from "./LocationDto";
import {ArtistDto} from "./ArtistDto";
import {ScheduleDto} from "./ScheduleDto";

export interface ConcertDto {
    id: string;
    locationDTO: LocationDto;
    artistDTO: ArtistDto;
    scheduleDTO: ScheduleDto;
}
