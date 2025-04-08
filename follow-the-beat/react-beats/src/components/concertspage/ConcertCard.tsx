import "./ConcertCard.css";

interface Concert {
  id: number;
  artist: string;
  location: string;
  startTime: string;
}

interface Props {
  concert: Concert;
  isInLineup: boolean;
  onAdd: (concert: Concert) => void;
  onRemove: (id: number) => void;
}

const ConcertCard = ({ concert, isInLineup, onAdd, onRemove }: Props) => {
  return (
    <div className={`concert-card ${isInLineup ? "selected" : ""}`}>
      <div className="concert-info">
        <p>
          <strong>{concert.artist}</strong> - {concert.location}
        </p>
        <p className="time">{new Date(concert.startTime).toLocaleString()}</p>
      </div>
      {!isInLineup ? (
        <button className="add-button" onClick={() => onAdd(concert)}>
          Add to lineup
        </button>
      ) : (
        <button className="remove-button" onClick={() => onRemove(concert.id)}>
          Remove from lineup
        </button>
      )}
    </div>
  );
};

export default ConcertCard;
