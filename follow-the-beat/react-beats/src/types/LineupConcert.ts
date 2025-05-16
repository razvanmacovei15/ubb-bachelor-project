import {ConcertDto} from "./ConcertDto.ts";

interface LineupConcert extends ConcertDto {
    details: string;
    compatibility: number;
}

export default LineupConcert;