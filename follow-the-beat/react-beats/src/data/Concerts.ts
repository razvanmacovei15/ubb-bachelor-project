import Concert from '../types/Concert';

const mockConcerts: Concert[] = Array.from({ length: 20 }, (_, i) => ({
    id: i + 1,
    artist: `Artist ${i + 1}`,
    location: `City ${i % 5}`,
    startTime: new Date(Date.now() + i * 3600000).toISOString(), // Future times
    compatibility: Math.floor(Math.random() * 101) // Random 0-100
}));

export default mockConcerts;