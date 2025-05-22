import React, { useEffect, useState } from "react";
import ConcertCard from "../components/concertspage/ConcertCard";
import "./FestivalsPage.css";
import Pagination from "../components/pagination/Pagination.tsx";
import { useConcertSortingFilteringContext } from "../contexts/ConcertSortingFiltering";
import FestivalConcertFilters from "../components/sidebars/FestivalConcertFilter.tsx";
import axios from "axios";
import { FestivalDto } from "../types/FestivalDto";
import {
  Carousel,
  CarouselApi,
  CarouselContent,
  CarouselNext,
  CarouselPrevious,
  CarouselItem,
} from "@/components/ui/carousel.tsx";
import { Card, CardContent } from "@/components/ui/card";
import { useLineupSortingFilteringContext } from "@/contexts/LineupSortingFilteringContext.tsx";

const FestivalsPage: React.FC = () => {
  // const API_URL = ""; // Remove the environment variable since we're using nginx proxy

  const [festivals, setFestivals] = useState<FestivalDto[]>([]);

  const { addLineupEntry, removeLineupEntry } =
    useLineupSortingFilteringContext();
  const onAddToLineup = async (concertId: string) => {
    console.log("[FestivalsPage] onAddToLineup called with:", concertId);

    try {
      if (!addLineupEntry) {
        console.warn("[FestivalsPage] addLineupEntry is undefined!");
      }

      await addLineupEntry({
        concertId,
        notes: "",
        priority: 0,
      });

      console.log("[FestivalsPage] addLineupEntry call completed");
    } catch (e) {
      console.error("[FestivalsPage] Failed to add concert to lineup", e);
    }
  };

  const {
    hasFestival,
    searchTerm,
    sortBy,
    sortDirection,
    concerts,
    totalCount,
    currentPage,
    itemsPerPage,
    setCurrentPage,
    festivalId,
    setFestivalId,
    resetAndSelectFestival,
    fetchConcerts,
    fetchFestivalConcerts,
  } = useConcertSortingFilteringContext();

  const [api, setApi] = React.useState<CarouselApi>();
  // @ts-ignore
  const [current, setCurrent] = React.useState(0);
  // @ts-ignore
  const [count, setCount] = React.useState(0);

  useEffect(() => {
    const fetchFestivals = async () => {
      try {
        const res = await axios.get<FestivalDto[]>("/api/festivals");
        console.log("Fetched festivals:", res.data);
        // Ensure we're working with an array
        const festivalsData = Array.isArray(res.data) ? res.data : [];
        setFestivals(festivalsData);
        if (festivalsData.length > 0) {
          setFestivalId(festivalsData[0].id);
        }
      } catch (e) {
        console.error("Failed to fetch festivals", e);
        setFestivals([]);
      }
    };

    fetchFestivals();
  }, []);

  useEffect(() => {
    if (festivalId) {
      fetchFestivalConcerts(festivalId);
    } else {
      fetchConcerts();
    }
  }, [
    searchTerm,
    sortBy,
    itemsPerPage,
    currentPage,
    festivalId,
    sortDirection,
  ]);

  useEffect(() => {
    setCurrentPage(1);
  }, [searchTerm, sortBy, itemsPerPage]);

  React.useEffect(() => {
    if (!api) {
      return;
    }

    setCount(api.scrollSnapList().length);
    setCurrent(api.selectedScrollSnap() + 1);

    api.on("select", () => {
      setCurrent(api.selectedScrollSnap() + 1);
    });
  }, [api]);

  return (
    <div className="page-background">
      <div className="festival-carousel">
        <Carousel
          opts={{
            align: "start",
          }}
          className="w-full h-full"
          setApi={setApi}
        >
          <CarouselContent className="h-full">
            {festivals.map((festival) => (
              <CarouselItem
                key={festival.id}
                className="sm:basis-1/1 md:basis-1/1 lg:basis-1/2 h-[40vh]"
              >
                <div
                  className="h-full p-2"
                  onClick={() => resetAndSelectFestival(festival.id)}
                >
                  <Card
                    className={`h-full cursor-pointer bg-cover bg-center ${
                      festivalId === festival.id
                        ? "ring-5 ring-[var(--color-accent)]"
                        : ""
                    }`}
                    style={{
                      backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)), url(${festival.festivalImageUrl})`,
                    }}
                    onClick={() => {
                      console.log(
                        "Festival image URL:",
                        festival.festivalImageUrl
                      );
                    }}
                  >
                    <CardContent className="h-full flex flex-col gap-2 justify-between text-white">
                      <h3 className="text-xl font-semibold">{festival.name}</h3>
                      <div className="text-sm flex-1">
                        <p className="text-gray-300 text-md flex items-center gap-2 mb-2">
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            className="h-6 w-6"
                            fill="none"
                            viewBox="0 0 24 24"
                            stroke="currentColor"
                          >
                            <path
                              strokeLinecap="round"
                              strokeLinejoin="round"
                              strokeWidth={2}
                              d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"
                            />
                            <path
                              strokeLinecap="round"
                              strokeLinejoin="round"
                              strokeWidth={2}
                              d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"
                            />
                          </svg>
                          {festival.location}
                        </p>
                        <p className="text-gray-300 text-md flex items-center gap-2">
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            className="h-6 w-6"
                            fill="none"
                            viewBox="0 0 24 24"
                            stroke="currentColor"
                          >
                            <path
                              strokeLinecap="round"
                              strokeLinejoin="round"
                              strokeWidth={2}
                              d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"
                            />
                          </svg>
                          {`${new Date(
                            festival.startDate
                          ).toLocaleDateString()} - ${new Date(
                            festival.endDate
                          ).toLocaleDateString()}`}
                        </p>
                      </div>
                      <a
                        className="underline mt-2 text-white"
                        href={festival.websiteUrl}
                        target="_blank"
                        rel="noopener noreferrer"
                      >
                        Visit Website
                      </a>
                    </CardContent>
                  </Card>
                </div>
              </CarouselItem>
            ))}
            <CarouselItem className="md:basis-1/2 lg:basis-1/3 h-full p-2">
              <div className="cursor-pointer h-full">
                <Card className="h-full">
                  <CardContent className="h-full flex flex-col justify-center items-center">
                    <h3 className="text-lg font-semibold">
                      More Festivals Coming Soon!
                    </h3>
                  </CardContent>
                </Card>
              </div>
            </CarouselItem>
          </CarouselContent>
          <CarouselPrevious />
          <CarouselNext />
        </Carousel>
      </div>

      <div className="content-container">
        <div className="concerts-container">
          {concerts.length === 0 ? (
            <h2>No concerts available. Please check back later.</h2>
          ) : (
            <>
              <div className="concert-grid">
                {concerts.map((concert) => (
                  <ConcertCard
                    key={concert.concertId}
                    hasFestival={hasFestival}
                    concert={concert}
                    onAdd={() => onAddToLineup(concert.concertId)}
                    onRemove={() => removeLineupEntry(concert.concertId)}
                  />
                ))}
              </div>
              <Pagination
                className="pagination-bar"
                currentPage={currentPage}
                totalCount={totalCount}
                pageSize={itemsPerPage}
                onPageChange={(page) => setCurrentPage(page)}
              />
            </>
          )}
        </div>
        <div className="flex flex-col gap-2 ">
          <FestivalConcertFilters />
        </div>
      </div>
    </div>
  );
};

export default FestivalsPage;
