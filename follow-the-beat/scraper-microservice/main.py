# main.py

import logging
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from scrapers.untold_scraper import scrape_untold_artists
from scrapers.electric_scraper import scrape_electric_castle
from scrapers.electric_scraper_unified import scrape_electric_castle_unified
from scrapers.untold_scraper_unified import scrape_untold_artists_unified
from shared import scrape_progress

# Setup logging (stream to console immediately)
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s",
    handlers=[logging.StreamHandler()]
)

# Create a logger object
logger = logging.getLogger(__name__)

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/untold")
def scrape_untold():
    logger.info("Received request: /untold")
    data = scrape_untold_artists()
    logger.info("Finished /untold request")
    return {"untold": data}

@app.get("/electric")
def scrape_ec():
    logger.info("Received request: /electric")
    data = scrape_electric_castle()
    logger.info("Finished /electric request")
    return {"electric": data}

@app.get("/untold-unified")
def scrape_untold():
    logger.info("Received request: /untold")
    data = scrape_untold_artists_unified()
    logger.info("Finished /untold request")
    return {"untold": data}

@app.get("/electric-unified")
def scrape_ec():
    logger.info("Received request: /electric")
    data = scrape_electric_castle_unified()
    logger.info("Finished /electric request")
    return {"electric": data}

@app.get("/progress/untold")
def get_untold_progress():
    """Check scraping progress for Untold"""
    return scrape_progress["untold"]
