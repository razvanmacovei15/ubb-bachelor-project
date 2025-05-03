package com.maco.followthebeat.v2.data.scrappers.untold.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.followthebeat.v2.data.scrappers.model.FestivalResponse;
import lombok.Data;

import java.util.List;

@Data
public class UntoldFestivalResponse implements FestivalResponse {

    @JsonProperty("festival_name")
    private String festivalName;

    @JsonProperty("date_range")
    private String dateRange;

    private String location;

    private List<UntoldArtist> artists;
}