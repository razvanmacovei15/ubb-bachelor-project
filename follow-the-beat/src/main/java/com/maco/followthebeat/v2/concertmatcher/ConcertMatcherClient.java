package com.maco.followthebeat.v2.concertmatcher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maco.followthebeat.v2.user.dto.UserEmbeddingPayloadDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import okhttp3.RequestBody;

import java.io.IOException;
import java.time.Duration;
@Slf4j
@Component
public class ConcertMatcherClient {

    @Value("${concert.matcher.api.url}")
    private String aiUrl;

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient.Builder()
            .callTimeout(Duration.ofSeconds(45))
            .connectTimeout(Duration.ofSeconds(45))
            .readTimeout(Duration.ofSeconds(45))
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MatchResponse matchConcerts(UserEmbeddingPayloadDto payload) throws IOException {
        String json = objectMapper.writeValueAsString(payload);
        log.debug("Sending to ConcertMatcher: {}", json);

        RequestBody body = RequestBody.create(JSON, json);
        Request httpRequest = new Request.Builder()
                .url(aiUrl)
                .post(body)
                .build();

        try (Response response = client.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code() + response.body());
            }
            String responseBody = response.body().string();
            log.debug("Received from ConcertMatcher: {}", responseBody);

            return objectMapper.readValue(responseBody, new TypeReference<>() {});        }
    }
}
