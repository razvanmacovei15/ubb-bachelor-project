import { ConcertDto } from "./ConcertDto";

export interface ScheduleDto {
  id: string;
  date: string;      // ISO date string
  startTime: string; // ISO time string
  concertDTO: ConcertDto;
}
