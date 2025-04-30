package com.maco.followthebeat.scrappers.untold.response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class UntoldParser {

    public static UntoldFestivalWrapper parseJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(json, UntoldFestivalWrapper.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Untold festival JSON", e);
        }
    }
}