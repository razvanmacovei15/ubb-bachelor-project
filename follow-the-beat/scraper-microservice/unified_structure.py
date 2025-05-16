# unified_structure.py

def create_festival_template():
    return {
        "festival_name": None,
        "description": None,
        "location": None,
        "start_date": None,  # Format: YYYY-MM-DD
        "end_date": None,    # Format: YYYY-MM-DD
        "logo_url": None,
        "website_url": None,
        "is_active": True,
        "artists": [],
    }

def create_artist_template():
    return {
        "name": None,
        "img_url": None,
        "stage_name": None,
        "date": None,
        "start_time": None
    }

