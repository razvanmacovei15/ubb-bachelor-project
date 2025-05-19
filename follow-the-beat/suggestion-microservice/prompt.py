from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
from fastapi.exceptions import RequestValidationError
from pydantic import BaseModel
from typing import List, Dict
from sentence_transformers import CrossEncoder

app = FastAPI()
model = CrossEncoder("cross-encoder/ms-marco-MiniLM-L-6-v2")


# --- DATA STRUCTURES ---

class FestivalArtist(BaseModel):
    concertId: str
    artistName: str
    genres: List[str]


class MatchRequest(BaseModel):
    requestId: str
    topUserArtists: List[str]
    genreFrequencies: Dict[str, int]
    festivalArtists: List[FestivalArtist]


class MatchResult(BaseModel):
    concertId: str
    artistName: str
    score: float


class MatchResponse(BaseModel):
    requestId: str
    matches: List[MatchResult]


# --- EXCEPTION HANDLER ---

@app.exception_handler(RequestValidationError)
async def validation_exception_handler(request: Request, exc: RequestValidationError):
    print("❌ Validation Error:")
    for err in exc.errors():
        print(f"  ➤ {err['loc']} - {err['msg']}")
    return JSONResponse(
        status_code=422,
        content={"detail": exc.errors()}
    )


# --- ENDPOINT ---

@app.post("/match-concerts", response_model=MatchResponse)
def match_concerts(req: MatchRequest):
    print(f"\n📥 Received Request:\nRequest ID: {req.requestId}")
    print(f"Top Artists: {req.topUserArtists}")
    print(f"Top Genres: {req.genreFrequencies}")
    print(f"Festival Artists (first 3): {[artist.dict() for artist in req.festivalArtists[:3]]}\n")

    user_text = build_user_text(req.topUserArtists, req.genreFrequencies)

    pairs = [
        (user_text, f"{artist.artistName} performs: {', '.join(artist.genres)}")
        for artist in req.festivalArtists
    ]

    scores = model.predict(pairs)

    min_score, max_score = min(scores), max(scores)
    if max_score != min_score:
        scores = [(s - min_score) / (max_score - min_score) for s in scores]
    else:
        scores = [0.5 for _ in scores]

    matches = [
        MatchResult(
            concertId=artist.concertId,
            artistName=artist.artistName,
            score=round(float(score), 4)
        )
        for artist, score in zip(req.festivalArtists, scores)
    ]

    matches.sort(key=lambda m: m.score, reverse=True)

    print(f"\n✅ Top Matches (Top 5):")
    for match in matches[:15]:
        print(f"  ➤ {match.artistName} | ID: {match.concertId} | Score: {match.score}")

    return MatchResponse(requestId=req.requestId, matches=matches)


# --- UTILITY ---

def build_user_text(artists: List[str], genres: Dict[str, int]) -> str:
    artist_str = ", ".join(artists[:10])
    genre_str = ", ".join([f"{g} ({c})" for g, c in sorted(genres.items(), key=lambda x: -x[1])[:10]])
    return f"User listens to artists like: {artist_str}. Top genres: {genre_str}."
