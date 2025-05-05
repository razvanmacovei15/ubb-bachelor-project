import {LocationDto} from "./LocationDTO";
import {ArtistDto} from "./ArtistDTO";
import {ScheduleDto} from "./ScheduleDTO";

export interface ConcertDto {
    id: string;
    locationDTO: LocationDto;
    artistDTO: ArtistDto;
    scheduleDTO: ScheduleDto;
}
