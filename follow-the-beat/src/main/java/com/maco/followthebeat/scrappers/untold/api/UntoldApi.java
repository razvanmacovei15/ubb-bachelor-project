package com.maco.followthebeat.scrappers.untold.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class UntoldApi {
    private final String UNTOLD_ENDPOINT = "http://localhost:8005/untold";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UntoldFestivalResponse fetchFestival() {
        try {
            String jsonResponse = makeHttpRequest();
            UntoldFestivalWrapper wrapper = objectMapper.readValue(jsonResponse, UntoldFestivalWrapper.class);


            if (wrapper == null ||  wrapper.getArtists() == null) {
                return null;
            }

            return wrapper.getArtists();

        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch or parse Untold festival data", e);
        }
    }

    private String makeHttpRequest() throws IOException {
        URL url = new URL(UNTOLD_ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Scanner scanner = new Scanner(connection.getInputStream());
        StringBuilder json = new StringBuilder();

        while (scanner.hasNextLine()) {
            json.append(scanner.nextLine());
        }

        scanner.close();
        connection.disconnect();
        return json.toString();
    }
}
