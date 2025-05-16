import "./LineupEntryCard.css";
import { Card, CardContent } from "@/components/ui/card.tsx";
import { LineupDetailDto } from "@/types/LineupDetailDto.ts";
import {Button} from "@/components/ui/button.tsx";
import {useLineupSortingFilteringContext} from "@/contexts/LineupSortingFilteringContext.tsx";

interface LineupConcertCardProps {
  lineupDetail: LineupDetailDto;
}

const LineupEntryCard = ({ lineupDetail }: LineupConcertCardProps) => {
  const showDate =
      lineupDetail.date && lineupDetail.startTime
          ? `${new Date(lineupDetail.date).toLocaleDateString()} • ${lineupDetail.startTime}`
          : "TO BE ANNOUNCED";

  const {removeLineupEntry} = useLineupSortingFilteringContext()

  return (
      <Card key={lineupDetail.id} className="hover:shadow-lg transition-shadow w-full">
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
                Compatibility: {lineupDetail.compatibility}%
              </span>
              )}
              {lineupDetail.priority !== null && (
                  <span className="bg-blue-100 text-blue-800 text-xs font-semibold px-2 py-1 rounded">
                Priority: {lineupDetail.priority}
              </span>
              )}
            </div>
          </div>
          <div className="flex flex-col sm:flex-row sm:items-center sm:gap-2 mt-4 sm:mt-0">
            {/* Spotify Button */}
            <a
                href={lineupDetail.spotifyUrl || "#"}
                target="_blank"
                rel="noopener noreferrer"
                className={`inline-block bg-[#1DB954] text-white text-sm font-medium px-4 py-2 rounded hover:bg-green-600 transition ${
                    !lineupDetail.spotifyUrl ? "pointer-events-none opacity-50" : ""
                }`}
            >
              Listen on Spotify
            </a>

            {/* Remove Button */}
            <Button
                onClick={() => {
                  removeLineupEntry(lineupDetail.id)
                }}
                variant="outline"
                className="text-sm font-semibold text-red-600 border-red-500 hover:bg-red-50 hover:border-red-600 mt-2 sm:mt-0"
            >
              Remove from lineup
            </Button>
          </div>
        </CardContent>
      </Card>
  );
};

export default LineupEntryCard;
