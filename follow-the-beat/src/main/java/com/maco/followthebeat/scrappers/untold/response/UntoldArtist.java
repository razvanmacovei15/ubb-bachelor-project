package com.maco.followthebeat.scrappers.untold.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UntoldArtist {

    private String name;
    private String stage;
    private String time;
    private String date;

    @JsonProperty("image_url")
    private String imageUrl;
}
