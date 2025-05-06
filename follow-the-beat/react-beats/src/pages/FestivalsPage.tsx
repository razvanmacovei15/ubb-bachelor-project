import React from "react";
import ConcertCard from "../components/concertspage/ConcertCard";
import "./FestivalsPage.css";
import Pagination from "../components/pagination/Pagination.tsx";
import { useConcertSortingFilteringContext } from "../contexts/ConcertSortingFiltering";
import FestivalConcertFilters from "../components/sidebars/FestivalConcertFilter.tsx";

import { FestivalDto } from "../types/FestivalDto";

import {Carousel, CarouselApi, CarouselContent, CarouselNext, CarouselPrevious, CarouselItem} from "@/components/ui/carousel.tsx";
import { Card, CardContent } from "@/components/ui/card"


const mockFestivals: FestivalDto[] = [
  {
    id: "f1",
    name: "Glastonbury",
    description: "Iconic UK music festival",
    location: "Somerset, UK",
    startDate: "2025-06-26",
    endDate: "2025-06-30",
    logoUrl: "https://upload.wikimedia.org/wikipedia/en/b/bd/Glastonbury_Festival_Logo.png",
    websiteUrl: "https://glastonburyfestivals.co.uk",
    isActive: true,
    dtoArtists: [],
    dtoStages: []
  },
  {
    id: "f2",
    name: "Lollapalooza",
    description: "Top US festival across genres",
    location: "Chicago, IL",
    startDate: "2025-07-28",
    endDate: "2025-07-31",
    logoUrl: "https://upload.wikimedia.org/wikipedia/commons/3/36/Lollapalooza_Logo.png",
    websiteUrl: "https://www.lollapalooza.com",
    isActive: true,
    dtoArtists: [],
    dtoStages: []
  },
];
const FestivalsPage: React.FC = () => {

  const {
    concerts,
    totalCount,
    currentPage,
    itemsPerPage,
    setCurrentPage
  } = useConcertSortingFilteringContext();

  const [api, setApi] = React.useState<CarouselApi>()
  // @ts-ignore
  const [current, setCurrent] = React.useState(0)
  // @ts-ignore
  const [count, setCount] = React.useState(0)

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
            {mockFestivals.map((festival) => (
                <CarouselItem key={festival.id} className="md:basis-1/2 lg:basis-1/3 h-[30vh]">
                  <div className="h-full">
                    <Card className="h-full">
                      <CardContent className="h-full flex flex-col justify-between">
                        <h3 className="text-lg font-semibold">{festival.name}</h3>
                        <div className="text-sm flex-1">
                          <p>{festival.description}</p>
                          <p>{festival.location}</p>
                          <p>{`${new Date(festival.startDate).toLocaleDateString()} - ${new Date(festival.endDate).toLocaleDateString()}`}</p>
                        </div>
                        <a className="underline mt-2" href={festival.websiteUrl} target="_blank" rel="noopener noreferrer">Visit Website</a>
                      </CardContent>
                    </Card>
                  </div>
                </CarouselItem>
            ))}
            <CarouselItem className="md:basis-1/2 lg:basis-1/3 h-full">
                <div>
                    <Card>
                      <CardContent>
                          <h3>More Festivals Coming Soon!</h3>
                          <p>Stay tuned for updates on upcoming festivals.</p>
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
