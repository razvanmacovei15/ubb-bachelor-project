import { addToLineup, removeFromLineup, editConcert, sortLineup, filterLineup } from "./LineupUtils";
import Concert from "../components/types/Concert";

const baseConcert: Concert = {
    id: 1,
    artist: "Zebra",
    location: "Berlin",
    startTime: new Date("2025-01-01T20:00:00Z").toISOString(),
    compatibility: 85
};

test("addToLineup creates default details field", () => {
    const result = addToLineup([], baseConcert);
    expect(result[0].details).toBe("");
});

test("removeFromLineup removes concert", () => {
    const lineup = addToLineup([], baseConcert);
    const result = removeFromLineup(lineup, 1);
    expect(result.length).toBe(0);
});

test("editConcert updates details", () => {
    let lineup = addToLineup([], baseConcert);
    lineup = editConcert(lineup, 1, "Bring a jacket");
    expect(lineup[0].details).toBe("Bring a jacket");
});

test("sortLineup by artist", () => {
    const lineup = [
        { ...baseConcert, id: 1, artist: "Zebra", details: "" },
        { ...baseConcert, id: 2, artist: "Alpha", details: "" }
    ];
    const sorted = sortLineup(lineup, "artist");
    expect(sorted[0].artist).toBe("Alpha");
});

test("filterLineup matches artist or location", () => {
    const lineup = [
        { ...baseConcert, id: 1, artist: "Coldplay", location: "London", details: "" },
        { ...baseConcert, id: 2, artist: "Radiohead", location: "Paris", details: "" }
    ];
    const result = filterLineup(lineup, "lon");
    expect(result.length).toBe(1);
    expect(result[0].artist).toBe("Coldplay");
});
