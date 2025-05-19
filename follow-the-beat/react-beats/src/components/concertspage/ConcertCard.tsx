import "./ConcertCard.css";
import {ConcertResponseDto} from "@/types/ConcertResponseDto.ts";
import {useState} from "react";
import {useUser} from "@/contexts/UserContext.tsx";
import {useLineupSortingFilteringContext} from "@/contexts/LineupSortingFilteringContext.tsx";

interface ConcertCardProps {
    concert: ConcertResponseDto;
    onAdd: (concert: ConcertResponseDto) => void;
    onRemove: (id: string) => void;
}

const ConcertCard = ({concert, onAdd, onRemove}: ConcertCardProps) => {

    const [imgError, setImgError] = useState(false);
    const {isConnectedToSpotify} = useUser();
    const {isConcertInLineup} = useLineupSortingFilteringContext();

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
    console.log("[ConcertCard] Render", {
        concertId: concert.concertId,
        isInLineup,
        isConnectedToSpotify,
    });

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

                            <button
                                className="add-button"
                                onClick={() => {
                                    console.log("[ConcertCard] Add clicked for:", concert.concertId);
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
    );
};

export default ConcertCard;
