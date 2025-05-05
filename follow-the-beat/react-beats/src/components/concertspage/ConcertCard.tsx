import "./ConcertCard.css";
import {ConcertDto} from "../../types/ConcertDto.ts";


interface ConcertCardProps {
  concert: ConcertDto;
  onAdd: (concert: ConcertDto) => void;
  onRemove: (id: string) => void;
}

const ConcertCard = ({ concert,  onAdd, onRemove }: ConcertCardProps) => {
  return (
    <div className={`concert-card`}>
      <div className="concert-info">
        <p>
          <strong>{concert.artistDTO.name}</strong> - {concert.locationDTO.name}
        </p>
        <p className="time">{new Date(concert.scheduleDTO.startTime).toLocaleString()}</p>
      </div>
      <button className="add-button" onClick={() => onAdd(concert)}>
        Add to lineup
      </button>
    </div>
  );
};

export default ConcertCard;
