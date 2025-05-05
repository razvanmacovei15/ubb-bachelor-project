import {ConcertDto} from "./ConcertDto.ts";

interface LineupConcert extends ConcertDto {
    details: string;
}

export default LineupConcert;