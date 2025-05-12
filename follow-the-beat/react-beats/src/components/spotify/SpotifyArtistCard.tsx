import SpotifyArtistDto from "@/types/SpotifyArtistDto.ts";
import React from "react";

interface SpotifyArtistCardProps {
    spotifyArtistDto: SpotifyArtistDto;
}

const SpotifyArtistCard = ({spotifyArtistDto} : SpotifyArtistCardProps) => {
    return (
        <div className="artist-card">
            <img src={spotifyArtistDto.imageUrl} alt={spotifyArtistDto.name} />
            <div className="artist-info">
                <span className="rank">#{spotifyArtistDto.rank}</span>
                <h4>{spotifyArtistDto.name}</h4>
            </div>
        </div>
    )
}

export default SpotifyArtistCard;