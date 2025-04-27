package com.maco.followthebeat.entity.spotify;

import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.entity.spotify.interfaces.BaseUserTopArtist;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "short_term_artist")
public class ShortTermArtist extends BaseUserTopArtist {
}
