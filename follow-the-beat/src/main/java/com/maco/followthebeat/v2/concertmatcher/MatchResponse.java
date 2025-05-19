package com.maco.followthebeat.v2.concertmatcher;

import java.util.List;
import java.util.UUID;

public class MatchResponse {
    public UUID requestId;
    public List<MatchResult> matches;
}
