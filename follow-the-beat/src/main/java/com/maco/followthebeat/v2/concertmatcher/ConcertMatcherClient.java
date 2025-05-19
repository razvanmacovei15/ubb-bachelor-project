package com.maco.followthebeat.v2.concertmatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maco.followthebeat.v2.user.dto.UserEmbeddingPayloadDto;
import lombok.AllArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import okhttp3.RequestBody;

import java.io.IOException;

@Component
public class ConcertMatcherClient {

    @Value("${ai.suggestor.url}")
    private String aiUrl;

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MatchResponse matchConcerts(UserEmbeddingPayloadDto payload) throws IOException {
        String json = objectMapper.writeValueAsString(payload);

        RequestBody body = RequestBody.create(JSON, json);
        Request httpRequest = new Request.Builder()
                .url(aiUrl)
                .post(body)
                .build();

        try (Response response = client.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }
            return objectMapper.readValue(response.body().string(), MatchResponse.class);
        }
    }
}
