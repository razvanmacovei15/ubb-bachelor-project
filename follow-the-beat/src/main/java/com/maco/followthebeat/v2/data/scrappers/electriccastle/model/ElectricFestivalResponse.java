package com.maco.followthebeat.v2.data.scrappers.electriccastle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.followthebeat.v2.data.scrappers.model.FestivalResponse;
import lombok.Data;

import java.util.List;
@Data
public class ElectricFestivalResponse implements FestivalResponse {
    @JsonProperty("festival_name")
    private String festivalName;
    private String location;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    private List<ElectricArtist> artists;
}
