import "./ConcertCard.css";
import { ConcertResponseDto } from "@/types/ConcertResponseDto.ts";
import { useState } from "react";
import { useUser } from "@/contexts/UserContext.tsx";
import { useLineupSortingFilteringContext } from "@/contexts/LineupSortingFilteringContext.tsx";

interface ConcertCardProps {
  concert: ConcertResponseDto;
  hasFestival: boolean;
  onAdd: (concert: ConcertResponseDto) => void;
  onRemove: (id: string) => void;
}

const ConcertCard = ({
  concert,
  onAdd,
  onRemove,
  hasFestival,
}: ConcertCardProps) => {
  const [imgError, setImgError] = useState(false);
  const { isConnectedToSpotify } = useUser();
  const { isConcertInLineup } = useLineupSortingFilteringContext();
  console.log("[ConcertCard] HAS FESTIVAL:", hasFestival);
  console.log("[ConcertCard] Concert:", concert.compatibility);
  const bgImage =
    !imgError && concert.artistImageUrl
      ? `url(${concert.artistImageUrl})`
      : `url(/images/image.jpg)`;

  const performanceDay = concert.date
    ? new Date(concert.date).toLocaleDateString(undefined, {
        weekday: "long",
        month: "short",
        day: "numeric",
      })
    : "TBA";

  const isInLineup = isConcertInLineup(concert.concertId);

  // Dynamic font size for long artist names
  const isLongTitle = concert.artistName.length > 10;

  return (
    <div
      className={`concert-card ${isInLineup ? "in-lineup" : ""}`}
      style={{ backgroundImage: bgImage }}
      onError={() => setImgError(true)}
    >
      <div className="concert-overlay flex flex-col justify-between h-full">
        <div
          className={`concert-info flex-grow p-4 ${
            isLongTitle ? "small-title" : ""
          }`}
        >
          <h3>{concert.artistName}</h3>
          <p className="datetime">{performanceDay}</p>
          <p className="location">Stage: {concert.stageName}</p>
        </div>
        <div className="button-group">
          {isConnectedToSpotify &&
            concert.compatibility !== null &&
            hasFestival && (
              <div className="compatibility-badge bg-green-100 text-green-800 text-[0.8rem] font-semibold px-2 py-1 w-full text-center">
                Compatibility: {Math.floor(concert.compatibility * 100)}%
                {/*<p className="compatibility-badge bg-green-100 text-green-800 text-[0.8rem] font-semibold px-2 py-1 w-full text-center">*/}
                {/*</p>*/}
              </div>
            )}
          {isConnectedToSpotify && (
            <div className="w-full">
              {isInLineup ? (
                <button
                  className="remove-button w-full"
                  onClick={() => onRemove(concert.concertId)}
                >
                  Remove from lineup
                </button>
              ) : (
                <button
                  className="add-button w-full"
                  onClick={() => {
                    console.log(
                      "[ConcertCard] Add clicked for:",
                      concert.concertId
                    );
                    onAdd(concert);
                  }}
                >
                  Add to lineup
                </button>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ConcertCard;
