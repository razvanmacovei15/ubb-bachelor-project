import "./LineupEntryCard.css";
import { Card, CardContent } from "@/components/ui/card.tsx";
import { LineupDetailDto } from "@/types/LineupDetailDto.ts";
import { Button } from "@/components/ui/button.tsx";
import { useLineupSortingFilteringContext } from "@/contexts/LineupSortingFilteringContext.tsx";
import { FaSpotify } from "react-icons/fa";

interface LineupConcertCardProps {
  lineupDetail: LineupDetailDto;
}

const LineupEntryCard = ({ lineupDetail }: LineupConcertCardProps) => {
  const showDate =
    lineupDetail.date && lineupDetail.startTime
      ? `${new Date(lineupDetail.date).toLocaleDateString()} • ${
          lineupDetail.startTime
        }`
      : "TO BE ANNOUNCED";

  const { removeLineupEntry } = useLineupSortingFilteringContext();

  console.log(lineupDetail.spotifyUrl);

  return (
    <Card
      key={lineupDetail.id}
      className="hover:shadow-lg transition-shadow w-full"
    >
      <CardContent className="p-2 flex flex-col sm:flex-row gap-2 items-start sm:items-center">
        {/* Artist Image */}
        <div className="w-30 h-30 shrink-0 rounded-full overflow-hidden border">
          <img
            src={lineupDetail.artistImageUrl || "/default-artist.png"}
            alt={lineupDetail.artistName}
            className="w-full h-full object-cover"
          />
        </div>

        {/* Text Content */}
        <div className="flex-1 w-full space-y-1">
          {/* Artist Name */}
          <h2 className="text-xl font-bold">{lineupDetail.artistName}</h2>

          {/* Date and Time */}
          <p className="text-sm text-gray-600">{showDate}</p>

          {/* Stage and Festival Info */}
          <p className="text-sm text-gray-600">
            Stage:{" "}
            <span className="font-medium">
              {lineupDetail.stageName || "Unknown Stage"}
            </span>{" "}
            @{" "}
            <span className="font-semibold">
              {lineupDetail.festivalName || "Unknown Festival"}
            </span>
          </p>

          {/* Notes */}
          {lineupDetail.notes && (
            <p className="text-sm text-gray-800 mt-1">
              <strong>Notes:</strong> {lineupDetail.notes}
            </p>
          )}

          {/* Badges */}
          <div className="flex flex-wrap gap-2 mt-2">
            {lineupDetail.compatibility !== null && (
              <span className="bg-green-100 text-green-800 text-xs font-semibold px-2 py-1 rounded">
                Compatibility: {Math.floor(lineupDetail.compatibility * 100)}%
              </span>
            )}
            {/* {lineupDetail.priority !== null && (
                            <span className="bg-blue-100 text-blue-800 text-xs font-semibold px-2 py-1 rounded">
                Priority: {lineupDetail.priority}
              </span>
                        )} */}
          </div>
        </div>
        <div className="flex flex-col sm:flex-row sm:items-center sm:gap-2 mt-4 sm:mt-0">
          {/* Spotify Button */}
          <a
            href={lineupDetail.spotifyUrl || "#"}
            target="_blank"
            rel="noopener noreferrer"
            className={`inline-flex items-center gap-2 px-4 py-2 rounded-full font-semibold text-white transition-all ${
              lineupDetail.spotifyUrl
                ? "bg-[#1DB954] hover:bg-[#1AA34A]"
                : "bg-gray-400 cursor-not-allowed opacity-60 pointer-events-none"
            }`}
          >
            <FaSpotify size={18} />
            <span>Listen on Spotify</span>
          </a>

          {/* Remove Button */}
          <Button
            onClick={() => removeLineupEntry(lineupDetail.concertId)}
            variant="outline"
            className="inline-flex items-center gap-2 px-4 py-2 rounded-full font-semibold text-red-600 border-red-500 hover:bg-red-50 hover:border-red-600 transition-all"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-4 w-4"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              strokeWidth={2}
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
            <span>Remove from Lineup</span>
          </Button>
        </div>
      </CardContent>
    </Card>
  );
};

export default LineupEntryCard;
