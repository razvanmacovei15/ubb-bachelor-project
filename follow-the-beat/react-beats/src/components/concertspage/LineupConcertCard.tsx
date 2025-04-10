import LineupConcert from "../types/LineupConcert";
import "./LineupConcertCard.css";

interface LineupConcertCardProps {
  concert: LineupConcert;
  getGradientClass: (compatibility: number) => string;
  handleUpdate: (id: number) => void;
  removeFromLineup: (id: number) => void;
}

const LineupConcertCard = ({
  concert,
  getGradientClass,
  handleUpdate,
  removeFromLineup,
}: LineupConcertCardProps) => {
  return (
    <div className="lineupconcertcard-container">
      <div className="lineupconcertcard-date">
        <p className="font-bold text-lg text-white overflow-ellipsis flex flex-col">
          {new Date(concert.startTime).getUTCDate().toLocaleString()}
          <p>Mar</p>
        </p>
      </div>
      <div className="lineupconcertcard-part-one">
        <img src="/images/image.jpg" alt={`${concert.artist} concert`} />
      </div>
      <div className="lineupcard-part-two">
        <div
          className={`w-full h-full p-4 text-white flex flex-col justify-between ${getGradientClass(
            concert.compatibility
          )}`}
        >
          <div>
            <p className="font-bold">{concert.artist}</p>
            <p>{concert.location}</p>
            <p className="text-sm text-gray-700">
              {new Date(concert.startTime).toLocaleString()}
            </p>
          </div>
        </div>
      </div>
      <div className="lineupcard-part-three">
        <button
          onClick={() => handleUpdate(concert.id)}
          className="w-full p-2 bg-blue-600 text-white rounded hover:bg-blue-700"
        >
          Update
        </button>
        <button
          onClick={() => removeFromLineup(concert.id)}
          className="w-full p-2 bg-red-600 text-white rounded hover:bg-red-700"
        >
          Remove
        </button>
      </div>
    </div>
  );
};

export default LineupConcertCard;
