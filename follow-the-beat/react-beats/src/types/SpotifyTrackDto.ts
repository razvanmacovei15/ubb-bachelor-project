import SpotifyArtistDto from "./SpotifyArtistDto";

type SpotifyTrackDto = {
    spotifyId: string;
    name: string;
    durationMs: number;
    popularity: number;
    previewUrl: string;
    albumImgUrl: string;
    rank: number;
    artists: SpotifyArtistDto[];
}

export default SpotifyTrackDto;