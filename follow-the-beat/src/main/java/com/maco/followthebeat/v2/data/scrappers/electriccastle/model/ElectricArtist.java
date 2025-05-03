package com.maco.followthebeat.v2.data.scrappers.electriccastle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;
@Data
public class ElectricArtist {
    private String name;
    private String stage;
    private String imgUrl;
    private String day;
    private int ranking;
    @JsonProperty("social_links")
    private Map<String, String> socialLinks;
}
