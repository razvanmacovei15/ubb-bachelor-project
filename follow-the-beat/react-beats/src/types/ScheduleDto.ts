import { ConcertResponseDto } from "./ConcertResponseDto.ts";

export interface ScheduleDto {
  id: string;
  date: string;      // ISO date string
  startTime: string; // ISO time string
  concertDTO: ConcertResponseDto;
}
