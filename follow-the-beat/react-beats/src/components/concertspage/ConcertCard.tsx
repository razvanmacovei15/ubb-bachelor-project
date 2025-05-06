import "./ConcertCard.css";
import { ConcertDto } from "../../types/ConcertDto.ts";
import { useState } from "react";

interface ConcertCardProps {
    concert: ConcertDto;
    onAdd: (concert: ConcertDto) => void;
    onRemove: (id: string) => void;
    isSelected?: boolean;
}

const ConcertCard = ({ concert, onAdd, onRemove, isSelected }: ConcertCardProps) => {
    const { artistDTO, locationDTO, scheduleDTO } = concert;
    const [imgError, setImgError] = useState(false);

    const bgImage = !imgError && artistDTO.imgUrl
        ? `url(${artistDTO.imgUrl})`
        : `url(/images/image.jpg)`;

    const performanceDay = scheduleDTO.date
        ? new Date(scheduleDTO.date).toLocaleDateString(undefined, { weekday: "long", month: "short", day: "numeric" })
        : "TBA";

    return (
        <div
            className={`concert-card ${isSelected ? "selected" : ""}`}
            style={{ backgroundImage: bgImage }}
            onError={() => setImgError(true)}
        >
            <div className="concert-overlay">
                <h3>{artistDTO.name}</h3>
                <p className="datetime">{performanceDay}</p>
                <p className="location">Stage: {locationDTO.name}</p>
                <div className="button-group">
                    {isSelected ? (
                        <button className="remove-button" onClick={() => onRemove(concert.id)}>Remove</button>
                    ) : (
                        <button className="add-button" onClick={() => onAdd(concert)}>Add to lineup</button>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ConcertCard;
