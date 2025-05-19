from fastapi import FastAPI
from pydantic import BaseModel
from typing import List, Dict
from sentence_transformers import CrossEncoder
import uuid

app = FastAPI()
model = CrossEncoder("cross-encoder/ms-marco-MiniLM-L-6-v2")


# --- DATA STRUCTURES ---

class FestivalArtist(BaseModel):
    concertId: str
    name: str
    genres: List[str]


class MatchRequest(BaseModel):
    requestId: str
    topUserArtists: List[str]
    genreFrequencies: Dict[str, int]
    festivalArtists: List[FestivalArtist]


class MatchResult(BaseModel):
    concertId: str
    name: str
    score: float


class MatchResponse(BaseModel):
    requestId: str
    matches: List[MatchResult]


# --- ENDPOINT ---

@app.post("/match-concerts", response_model=MatchResponse)
def match_concerts(req: MatchRequest):
    # 1. Build user profile string
    user_text = build_user_text(req.topUserArtists, req.genreFrequencies)

    # 2. Create input pairs for CrossEncoder
    pairs = [
        (user_text, f"{artist.name} performs: {', '.join(artist.genres)}")
        for artist in req.festivalArtists
    ]

    # 3. Predict similarity scores
    scores = model.predict(pairs)

    # Normalize scores
    min_score, max_score = min(scores), max(scores)
    if max_score != min_score:
        scores = [(s - min_score) / (max_score - min_score) for s in scores]
    else:
        scores = [0.5 for _ in scores]

    # 4. Zip back into results
    matches = [
        MatchResult(
            concertId=artist.concertId,
            name=artist.name,
            score=round(float(score), 4)
        )
        for artist, score in zip(req.festivalArtists, scores)
    ]

    # 5. Sort descending
    matches.sort(key=lambda m: m.score, reverse=True)

    return MatchResponse(requestId=req.requestId, matches=matches)


# --- UTILITY ---

def build_user_text(artists: List[str], genres: Dict[str, int]) -> str:
    artist_str = ", ".join(artists[:10])
    genre_str = ", ".join([f"{g} ({c})" for g, c in sorted(genres.items(), key=lambda x: -x[1])[:10]])
    return f"User listens to artists like: {artist_str}. Top genres: {genre_str}."
