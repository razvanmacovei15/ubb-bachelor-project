import time as time_module
import logging
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException

from unified_structure import create_festival_template, create_artist_template

logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")
logger = logging.getLogger(__name__)

def setup_driver():
    chrome_options = Options()
    chrome_options.add_argument("--headless")
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument("--disable-dev-shm-usage")
    chrome_options.add_argument("--window-size=1920,1080")
    return webdriver.Chrome(options=chrome_options)

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

def scrape_untold_artists_unified():
    homepage_url = "https://untold.com/"
    artist_url = "https://untold.com/artists"

    driver = setup_driver()
    artists = []

    try:
        logger.info("Loading Untold homepage...")
        driver.get(homepage_url)

        try:
            date_range = driver.find_element(By.CSS_SELECTOR, "h2.sc-e665f020-0.jFABKF").text
        except:
            date_range = "Unknown Dates"

        try:
            location = driver.find_element(By.CSS_SELECTOR, "p.sc-e665f020-0.ktjDcS").text
        except:
            location = "Unknown Location"

        festival = create_festival_template()
        festival.update({
            "festival_name": "Untold 2025",
            "location": location,
            "start_date": "2025-08-07",
            "end_date": "2025-08-10"
        })

        logger.info("Navigating to Untold artists page...")
        driver.get(artist_url)

        WebDriverWait(driver, 15).until(
            EC.presence_of_all_elements_located((By.CLASS_NAME, "sc-b9dcd1e1-2"))
        )

        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time_module.sleep(3)

        artist_cards = driver.find_elements(By.CLASS_NAME, "sc-b9dcd1e1-2")

        for card in artist_cards:
            artist_info = extract_artist_info(card)

            artist_entry = create_artist_template()
            artist_entry["name"] = artist_info["name"]
            artist_entry["img_url"] = artist_info["image_url"]
            artist_entry["stage_name"] = artist_info["stage"]
            artist_entry["date"] = artist_info["date"]
            artist_entry["start_time"] = artist_info["time"]

            festival["artists"].append(artist_entry)

        return festival

    except TimeoutException:
        return {"error": "Timeout while loading artist page"}
    except Exception as e:
        return {"error": str(e)}
    finally:
        driver.quit()