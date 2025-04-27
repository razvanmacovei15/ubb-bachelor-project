package com.maco.followthebeat.v1.spotify;

import com.maco.followthebeat.v1.spotify.interfaces.BaseUserTopTrack;
import jakarta.persistence.*;

@Entity
@Table(name = "long_term_track")
public class LongTermTrack extends BaseUserTopTrack {
} 