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
}

const ConcertCard = ({ concert, isInLineup, onAdd }: Props) => {
  return (
    <div className={`concert-card ${isInLineup ? "selected" : ""}`}>
      <div className="concert-info">
        <p>
          <strong>{concert.artist}</strong> - {concert.location}
        </p>
        <p className="time">{new Date(concert.startTime).toLocaleString()}</p>
      </div>
      <button className="add-button" onClick={() => onAdd(concert)}>
        Add
      </button>
    </div>
  );
};

export default ConcertCard;
