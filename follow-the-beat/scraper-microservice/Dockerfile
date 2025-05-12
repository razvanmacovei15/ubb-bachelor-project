FROM python:3.11-slim

ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

# Install system dependencies
RUN apt-get update && apt-get install -y \
    wget unzip curl gnupg \
    fonts-liberation libappindicator3-1 libasound2 libatk-bridge2.0-0 \
    libatk1.0-0 libcups2 libdbus-1-3 libgdk-pixbuf2.0-0 \
    libnspr4 libnss3 libxcomposite1 libxdamage1 libxrandr2 xdg-utils \
    libu2f-udev libvulkan1 libxss1 libgbm1 \
    && rm -rf /var/lib/apt/lists/*

# Install Chrome for Testing (v114)
RUN wget https://storage.googleapis.com/chrome-for-testing-public/136.0.7103.49/linux64/chrome-linux64.zip && \
    unzip chrome-linux64.zip && \
    mv chrome-linux64 /opt/chrome && \
    ln -s /opt/chrome/chrome /usr/bin/google-chrome && \
    rm chrome-linux64.zip

# Install ChromeDriver for Testing (v114)
RUN wget https://storage.googleapis.com/chrome-for-testing-public/136.0.7103.49/linux64/chromedriver-linux64.zip && \
    unzip chromedriver-linux64.zip && \
    mv chromedriver-linux64/chromedriver /usr/local/bin/chromedriver && \
    chmod +x /usr/local/bin/chromedriver && \
    rm -rf chromedriver-linux64*

# Set workdir
WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8070

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8070"]
