import { ConcertDto } from "./ConcertDTO";

export interface ScheduleDto {
  id: string;
  date: string;      // ISO date string
  startTime: string; // ISO time string
  concertDTO: ConcertDto;
}
