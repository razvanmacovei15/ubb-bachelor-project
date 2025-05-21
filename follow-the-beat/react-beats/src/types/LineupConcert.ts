import {ConcertResponseDto} from "./ConcertResponseDto.ts";

interface LineupConcert extends ConcertResponseDto {
    details: string;
    compatibility: number;
}

export default LineupConcert;