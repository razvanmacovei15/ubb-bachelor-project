import { useEffect, useRef, useState } from "react";
import { useLineup } from "../components/contexts/LineupContext";

let nextId = 1000;

const generateRandomConcert = () => ({
  id: nextId++,
  artist: `Artist ${Math.floor(Math.random() * 1000)}`,
  location: `City ${Math.floor(Math.random() * 10)}`,
  startTime: new Date(Date.now() + Math.random() * 1e7).toISOString(),
  compatibility: Math.floor(Math.random() * 101),
});

export const useSimulateLineupChanges = () => {
  const { addToLineup } = useLineup();
  const [isRunning, setIsRunning] = useState(false);
  const intervalRef = useRef<NodeJS.Timeout | null>(null);

  const start = () => {
    if (intervalRef.current) return; 
    intervalRef.current = setInterval(() => {
      const concert = generateRandomConcert();
      addToLineup(concert);
    }, 500);
    setIsRunning(true);
  };

  const stop = () => {
    if (intervalRef.current) {
      clearInterval(intervalRef.current);
      intervalRef.current = null;
      setIsRunning(false);
    }
  };

  const toggle = () => {
    isRunning ? stop() : start();
  };

  useEffect(() => {
    return () => {
      stop(); 
    };
  }, []);

  return { isRunning, toggle };
};
