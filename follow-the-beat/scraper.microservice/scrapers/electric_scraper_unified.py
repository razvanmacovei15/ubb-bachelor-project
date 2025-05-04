import logging
import requests
from unified_structure import create_festival_template, create_artist_template, create_concert_template

logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")
logger = logging.getLogger(__name__)

def scrape_electric_castle_unified():
    url = "https://api2.electriccastle.ro/api/rest/egrpeJG5xhCfv3rmRD8r/json.php?type=lineup"

    try:
        logger.info("Starting Electric Castle scraping...")
        response = requests.get(url, timeout=10)
        response.raise_for_status()
        data = response.json()
        logger.info(f"Found {len(data)} artists")

        festival = create_festival_template()
        festival.update({
            "festival_name": "Electric Castle 2025",
            "location": "Bontida, Romania",
            "start_date": "2025-07-16",
            "end_date": "2025-07-20",
        })

        for artist in data:
            artist_entry = create_artist_template()
            artist_entry["name"] = artist.get("name", "Unknown Artist")
            artist_entry["img_url"] = artist.get("image")

            schedule = artist.get("schedule", [{}])[0] if artist.get("schedule") else {}
            concert = create_concert_template()
            concert["stage_name"] = schedule.get("stage", "Unknown Stage")
            concert["date"] = schedule.get("day", "Unknown Day")

            artist_entry["concerts"].append(concert)
            festival["artists"].append(artist_entry)

        logger.info("Finished processing Electric Castle artists")
        return festival

    except Exception as e:
        logger.error(f"Error during Electric Castle scraping: {str(e)}")
        return {"error": str(e)}