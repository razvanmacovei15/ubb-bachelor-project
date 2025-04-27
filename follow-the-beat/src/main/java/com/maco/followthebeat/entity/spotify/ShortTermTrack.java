package com.maco.followthebeat.entity.spotify;

import com.maco.followthebeat.entity.spotify.interfaces.BaseUserTopTrack;
import jakarta.persistence.*;

@Entity
@Table(name = "short_term_track")
public class ShortTermTrack extends BaseUserTopTrack {
}