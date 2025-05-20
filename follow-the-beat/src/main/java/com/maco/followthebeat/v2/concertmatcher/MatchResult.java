package com.maco.followthebeat.v2.concertmatcher;

import lombok.Data;

@Data
public class MatchResult {
    public String concertId;
    public String artistName;
    public float score;
}
