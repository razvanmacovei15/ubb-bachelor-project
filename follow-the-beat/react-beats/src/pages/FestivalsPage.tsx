import React, {useEffect, useState} from "react";
import ConcertCard from "../components/concertspage/ConcertCard";
import "./FestivalsPage.css";
import Pagination from "../components/pagination/Pagination.tsx";
import { useConcertSortingFilteringContext } from "../contexts/ConcertSortingFiltering";
import FestivalConcertFilters from "../components/sidebars/FestivalConcertFilter.tsx";
import axios from "axios";
import { FestivalDto } from "../types/FestivalDto";
import {Carousel, CarouselApi, CarouselContent, CarouselNext, CarouselPrevious, CarouselItem} from "@/components/ui/carousel.tsx";
import { Card, CardContent } from "@/components/ui/card"

const FestivalsPage: React.FC = () => {
const API_URL = import.meta.env.VITE_API_URL;

    const [festivals, setFestivals] = useState<FestivalDto[]>([]);



  const {
    concerts,
    totalCount,
    currentPage,
    itemsPerPage,
    setCurrentPage,
      festivalId,
      setFestivalId,
      resetAndSelectFestival,
      fetchConcerts,
  } = useConcertSortingFilteringContext();

  const [api, setApi] = React.useState<CarouselApi>()
  // @ts-ignore
  const [current, setCurrent] = React.useState(0)
  // @ts-ignore
  const [count, setCount] = React.useState(0)

    useEffect(() => {
        const fetchFestivals = async () => {
            const sessionToken = localStorage.getItem("sessionToken");
            try {
                const res = await axios.get<FestivalDto[]>(`${API_URL}/api/festivals`, {
                    headers: { Authorization: `Bearer ${sessionToken}` }
                });
                console.log("Fetched festivals:", res.data);
                setFestivals(res.data);
            } catch (e) {
                console.error("Failed to fetch festivals", e);
            }
        };

        fetchFestivals();
        fetchConcerts();
    }, []);
  React.useEffect(() => {
    if (!api) {
      return
    }

    setCount(api.scrollSnapList().length)
    setCurrent(api.selectedScrollSnap() + 1)

    api.on("select", () => {
      setCurrent(api.selectedScrollSnap() + 1)
    })
  }, [api])

  return (
    <div className="page-background">
      <div className="festival-carousel">
        <Carousel opts={{
          align: "start",
        }}
                  className="w-full h-full" setApi={setApi} >
          <CarouselContent className="h-full">
            {festivals.map((festival) => (
                <CarouselItem key={festival.id} className="md:basis-1/2 lg:basis-1/3 h-[30vh]">
                    <div
                        className="h-full p-2"
                        onClick={() => resetAndSelectFestival(festival.id)}
                    >
                        <Card className={`h-full cursor-pointer ${festivalId === festival.id ? "ring-3 ring-[var(--color-accent)]" : ""}`}>
                            <CardContent className="h-full flex flex-col justify-between">
                                <h3 className="text-lg font-semibold">{festival.name}</h3>
                                <div className="text-sm flex-1">
                                    <p>{festival.description}</p>
                                    <p>{festival.location}</p>
                                    <p>
                                        {`${new Date(festival.startDate).toLocaleDateString()} - ${new Date(festival.endDate).toLocaleDateString()}`}
                                    </p>
                                </div>
                                <a className="underline mt-2" href={festival.websiteUrl} target="_blank"
                                   rel="noopener noreferrer">
                                    Visit Website
                                </a>
                            </CardContent>
                        </Card>
                    </div>
                </CarouselItem>
            ))}
              <CarouselItem className="md:basis-1/2 lg:basis-1/3 h-full p-2">
                  <div className="cursor-pointer h-full" onClick={() => setFestivalId(null)}>
                      <Card className="h-full">
                          <CardContent className="h-full flex flex-col justify-center items-center">
                              <h3 className="text-lg font-semibold">More Festivals Coming Soon!</h3>
                              <p className="text-sm text-center">Click to reset and view all concerts again.</p>
                          </CardContent>
                      </Card>
                  </div>
              </CarouselItem>
          </CarouselContent>
            <CarouselPrevious/>
            <CarouselNext/>
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
                          key={concert.id}
                          concert={concert}
                          onAdd={() => console.log("Add to lineup")}
                          onRemove={() => console.log("Remove from lineup")}
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
        <FestivalConcertFilters />
      </div>
    </div>
  );
};

export default FestivalsPage;
