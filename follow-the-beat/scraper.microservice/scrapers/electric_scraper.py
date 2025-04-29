# scrapers/electric_scraper.py

import logging
import requests

# Setup logging
logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")
logger = logging.getLogger(__name__)

def scrape_electric_castle():
    url = "https://api2.electriccastle.ro/api/rest/egrpeJG5xhCfv3rmRD8r/json.php?type=lineup"

    try:
        logger.info("Starting Electric Castle scraping...")
        response = requests.get(url, timeout=10)
        response.raise_for_status()
        logger.info("Successfully fetched Electric Castle lineup JSON")

        data = response.json()
        logger.info(f"Found {len(data)} artists")

        artists_data = []
        for artist in data:
            artist_info = {
                "name": artist.get("name", "Unknown Artist"),
                "stage": artist.get("schedule", [{}])[0].get("stage", "Unknown Stage") if artist.get("schedule") else "Unknown Stage",
                "image_url": artist.get("image"),
                "day": artist.get("schedule", [{}])[0].get("day", "Unknown Day") if artist.get("schedule") else "Unknown Day",
                "ranking": artist.get("ranking", 0),
                "social_links": {
                    "website": artist.get("websiteUrl"),
                    "facebook": artist.get("facebookUrl"),
                    "instagram": artist.get("instagramUrl"),
                    "youtube": artist.get("youtubeUrl"),
                    "spotify": artist.get("spotifyUrl")
                }
            }
            artists_data.append(artist_info)

        logger.info("Finished processing Electric Castle artists")
        return artists_data

    except Exception as e:
        logger.error(f"Error during Electric Castle scraping: {str(e)}")
        return {"error": str(e)}
