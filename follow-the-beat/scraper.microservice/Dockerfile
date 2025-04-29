# Use official lightweight Python image
FROM python:3.11-slim

# Install Chrome & ChromeDriver for Selenium (important for headless browsing)
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    curl \
    chromium-driver \
    chromium \
    fonts-liberation \
    libappindicator3-1 \
    libasound2 \
    libatk-bridge2.0-0 \
    libnspr4 \
    libnss3 \
    libx11-xcb1 \
    libxcomposite1 \
    libxcursor1 \
    libxdamage1 \
    libxrandr2 \
    xdg-utils

# Set display for headless mode
ENV DISPLAY=:99

# Set working directory inside container
WORKDIR /app

# Copy dependency list first and install them
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy all project files
COPY . .

# Expose the app port (FastAPI listens on 8000)
EXPOSE 8005

# Start FastAPI server
CMD ["python", "-u", "-m", "uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8005"]
