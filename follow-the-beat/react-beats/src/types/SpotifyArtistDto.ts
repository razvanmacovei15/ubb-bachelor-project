type SpotifyArtistDto = {
    spotifyId: string;
    name: string;
    genres: string[];
    popularity: number;
    imageUrl: string;
    rank: number;
    playCount: number;
}

export default SpotifyArtistDto;