package com.maco.followthebeat.v2.data.scrappers.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maco.followthebeat.v2.data.scrappers.model.FestivalResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class GenericApi {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T extends FestivalResponse> T fetchFestival(String endpointUrl, Class<T> responseClass, String wrapperFieldName) {
        try {
            String jsonResponse = makeHttpRequest(endpointUrl);
            JsonNode tree = objectMapper.readTree(jsonResponse);
            JsonNode festivalNode = tree.get(wrapperFieldName);
            return objectMapper.treeToValue(festivalNode, responseClass);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch or parse festival data", e);
        }
    }

    private String makeHttpRequest(String urlStr) throws IOException {
        try(HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlStr))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException("Failed to fetch data. Status code: " + response.statusCode());
            }

            return response.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }
}
