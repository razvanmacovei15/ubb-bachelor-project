package com.maco.followthebeat.entity.spotify;

import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.entity.spotify.interfaces.BaseUserTopTrack;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "long_term_track")
public class LongTermTrack extends BaseUserTopTrack {
} 