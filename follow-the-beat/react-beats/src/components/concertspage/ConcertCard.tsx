import "./ConcertCard.css";
import {ConcertDto} from "../../types/ConcertDto.ts";
import {useState} from "react";
import {useUser} from "@/contexts/UserContext.tsx";
import {useLineupSortingFilteringContext} from "@/contexts/LineupSortingFilteringContext.tsx";

interface ConcertCardProps {
    concert: ConcertDto;
    onAdd: (concert: ConcertDto) => void;
    onRemove: (id: string) => void;
}

const ConcertCard = ({concert, onAdd, onRemove}: ConcertCardProps) => {
    const {artistDTO, locationDTO, scheduleDTO} = concert;
    const [imgError, setImgError] = useState(false);
    const {isConnectedToSpotify} = useUser();
    const {isConcertInLineup, removeLineupEntry} = useLineupSortingFilteringContext();

    const bgImage =
        !imgError && artistDTO.imgUrl
            ? `url(${artistDTO.imgUrl})`
            : `url(/images/image.jpg)`;

    const performanceDay = scheduleDTO.date
        ? new Date(scheduleDTO.date).toLocaleDateString(undefined, {
            weekday: "long",
            month: "short",
            day: "numeric",
        })
        : "TBA";

    const isInLineup = isConcertInLineup(concert.id);
    console.log("[ConcertCard] Render", {
        concertId: concert.id,
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
                <h3>{artistDTO.name}</h3>
                <p className="datetime">{performanceDay}</p>
                <p className="location">Stage: {locationDTO.name}</p>
                {isConnectedToSpotify && (
                    <div className="button-group">
                        {isInLineup ? (
                            <button
                                className="remove-button"
                                onClick={() => onRemove(concert.id)}
                            >
                                Remove from lineup
                            </button>
                        ) : (

                            <button
                                className="add-button"
                                onClick={() => {
                                    console.log("[ConcertCard] Add clicked for:", concert.id);
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
