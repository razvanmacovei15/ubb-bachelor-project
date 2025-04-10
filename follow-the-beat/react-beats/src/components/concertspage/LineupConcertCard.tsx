import { useState } from "react";
import LineupConcert from "../types/LineupConcert";

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
  const [details, setDetails] = useState<string>(concert.details);

  return (
    <>
      <div
        className={`w-4/5 p-4 text-black flex flex-col justify-between ${getGradientClass(
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

        <textarea
          placeholder="Add details..."
          value={concert.details}
          onChange={(e) => {
            setDetails(e.target.value);
            concert.details = e.target.value;
          }}
          className="mt-2 w-full p-2 border rounded"
        />
      </div>

      <div className="w-1/5 flex flex-col items-center justify-end gap-2 p-2">
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
    </>
  );
};

export default LineupConcertCard;
