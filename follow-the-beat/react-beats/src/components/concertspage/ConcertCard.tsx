import "./ConcertCard.css";
import {ConcertResponseDto} from "@/types/ConcertResponseDto.ts";
import {useState} from "react";
import {useUser} from "@/contexts/UserContext.tsx";
import {useLineupSortingFilteringContext} from "@/contexts/LineupSortingFilteringContext.tsx";
import {useConcertSortingFilteringContext} from "@/contexts/ConcertSortingFiltering.tsx";

interface ConcertCardProps {
    concert: ConcertResponseDto;
    hasFestival: boolean;
    onAdd: (concert: ConcertResponseDto) => void;
    onRemove: (id: string) => void;
}

const ConcertCard = ({concert, onAdd, onRemove}: ConcertCardProps) => {

    const [imgError, setImgError] = useState(false);
    const {isConnectedToSpotify} = useUser();
    const {isConcertInLineup} = useLineupSortingFilteringContext();
    const {hasFestival} = useConcertSortingFilteringContext()
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

    return (
        <div
            className={`concert-card ${isInLineup ? "in-lineup" : ""}`}
            style={{backgroundImage: bgImage}}
            onError={() => setImgError(true)}
        >
            <div className="concert-overlay">
                <h3>{concert.artistName}</h3>
                <p className="datetime">{performanceDay}</p>
                <p className="location">Stage: {concert.stageName}</p>
                {isConnectedToSpotify && (
                    <div className="button-group">
                        {isInLineup ? (
                            <button
                                className="remove-button"
                                onClick={() => onRemove(concert.concertId)}
                            >
                                Remove from lineup
                            </button>
                        ) : (
                            <div>
                                <div className="flex flex-wrap gap-2 mt-2">
                                    {concert.compatibility !== null || hasFestival && (
                                        <span
                                            className="bg-green-100 text-green-800 text-xs font-semibold px-2 py-1 rounded">
        Compatibility: {concert.compatibility}%
      </span>
                                    )}
                                </div>
                                <button
                                    className="add-button"
                                    onClick={() => {
                                        console.log("[ConcertCard] Add clicked for:", concert.concertId);
                                        onAdd(concert);
                                    }}
                                >
                                    Add to lineup
                                </button>
                            </div>


                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

export default ConcertCard;
