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
        "artists": [],  # List of artist objects
        "stages": []     # List of stage objects (optional)
    }

def create_artist_template():
    return {
        "name": None,
        "img_url": None,
        "genres": [],
        "concerts": []  # List of concert objects
    }

def create_concert_template():
    return {
        "stage_name": None,
        "date": None,        # Format: YYYY-MM-DD
        "start_time": None   # Format: HH:MM (24h) or None
    }

def create_stage_template():
    return {
        "name": None
    }
