import SpotifyTrackDto from "../../types/SpotifyTrackDto.ts"

interface SpotifyTrackCardProps {
    spotifyTrackDto: SpotifyTrackDto;
}
const SpotifyTrackCard = ({spotifyTrackDto} : SpotifyTrackCardProps) => {
    return (
        <div key={spotifyTrackDto.spotifyId} className="track-item" >
            <span className="rank">#{spotifyTrackDto.rank}</span>
            <img src={spotifyTrackDto.albumImgUrl} alt={spotifyTrackDto.name} />
            <div className="track-info">
                <h4>{spotifyTrackDto.name}</h4>
                <p>{spotifyTrackDto.artists.map((a) => a.name).join(", ")}</p>
            </div>
        </div>
    )
}

export default SpotifyTrackCard;