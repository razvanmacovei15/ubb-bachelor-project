import {ConcertResp} from "./ConcertResponseDto.ts";

interface LineupConcert extends ConcertResp {
    details: string;
    compatibility: number;
}

export default LineupConcert;