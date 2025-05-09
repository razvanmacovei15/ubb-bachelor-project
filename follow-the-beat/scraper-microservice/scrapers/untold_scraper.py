# scrapers/untold_scraper.py
from shared import scrape_progress
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
import time as time_module

import logging
# Setup logging
logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")
logger = logging.getLogger(__name__)
def setup_driver():
    chrome_options = Options()
    chrome_options.add_argument("--headless")
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument("--disable-dev-shm-usage")
    chrome_options.add_argument("--window-size=1920,1080")

    return webdriver.Chrome(options=chrome_options)

def get_festival_info(driver):
    """Extracts date range and location from the Untold homepage."""
    try:
        date_range = driver.find_element(By.CSS_SELECTOR, "h2.sc-e665f020-0.jFABKF").text
        logger.info(f"Date range: {date_range}")
    except Exception as e:
        logger.warning(f"Could not find date range: {str(e)}")
        date_range = "Unknown Dates"

    try:
        location = driver.find_element(By.CSS_SELECTOR, "p.sc-e665f020-0.ktjDcS").text
        logger.info(f"Location: {location}")
    except Exception as e:
        logger.warning(f"Could not find location: {str(e)}")
        location = "Unknown Location"

    return date_range, location



def extract_artist_info(card):
    def get_text(by, selector, fallback="Unknown"):
        try:
            return card.find_element(by, selector).text
        except:
            return fallback

    def get_img():
        try:
            img = card.find_element(By.CSS_SELECTOR, "img")
            return img.get_attribute("src")
        except:
            return None

    def get_time_and_date():
        try:
            info_divs = card.find_elements(By.CSS_SELECTOR, ".sc-b9dcd1e1-5.WcBUR")
            time_text = info_divs[0].find_element(By.TAG_NAME, "span").text if len(info_divs) > 0 else "Unknown"
            date_text = info_divs[1].find_element(By.TAG_NAME, "span").text if len(info_divs) > 1 else "Unknown"
            return time_text, date_text
        except:
            return "Unknown", "Unknown"

    time, date = get_time_and_date()

    return {
        "name": get_text(By.CLASS_NAME, "sc-b9dcd1e1-1"),
        "stage": get_text(By.CLASS_NAME, "bLfhPn"),
        "time": time,
        "date": date,
        "image_url": get_img()
    }

def scrape_untold_artists():
    homepage_url = "https://untold.com/"
    artist_url = "https://untold.com/artists"

    driver = setup_driver()
    artists = []

    try:
        scrape_progress["untold"]["status"] = "in progress"
        scrape_progress["untold"]["percent"] = 0

        # Step 1: Get general info
        logger.info("Loading Untold homepage...")
        driver.get(homepage_url)

        # 👇 Extract the general festival info
        date_range, location = get_festival_info(driver)

        # Step 2: Artists page
        logger.info("Navigating to Untold artists page...")
        driver.get(artist_url)

        WebDriverWait(driver, 15).until(
            EC.presence_of_all_elements_located((By.CLASS_NAME, "sc-b9dcd1e1-2"))
        )

        logger.info("Found artist cards — scrolling to trigger lazy load")
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time_module.sleep(3)

        artist_cards = driver.find_elements(By.CLASS_NAME, "sc-b9dcd1e1-2")
        logger.info(f"Found {len(artist_cards)} artists")

        total = len(artist_cards)
        for i, card in enumerate(artist_cards):
            artist = extract_artist_info(card)
            artists.append(artist)

            # Update progress
            scrape_progress["untold"]["percent"] = int(((i + 1) / total) * 100)

        scrape_progress["untold"]["status"] = "done"
        logger.info("Finished scraping Untold artists.")

    except TimeoutException:
        scrape_progress["untold"]["status"] = "error"
        logger.error("Timeout loading artist page")
        return {"error": "Timeout while loading artist page"}
    except Exception as e:
        scrape_progress["untold"]["status"] = "error"
        logger.error(f"Unexpected error: {str(e)}")
        return {"error": str(e)}
    finally:
        driver.quit()
        logger.info("Closed browser")

    return {
        "festival_name": "Untold 2025",
        "date_range": date_range,
        "location": location,
        "artists": artists
    }