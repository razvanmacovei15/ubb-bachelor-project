package com.maco.followthebeat.v2.core.entity;

import com.maco.followthebeat.v2.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "lineup_entries", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "concert_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineupEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    @Column(name = "notes")
    private String notes; // optional: user can add notes

    @Column(name = "priority")
    private Integer priority; // optional: rank or priority in lineup

    @Column(name = "compatibility")
    private Integer compatibility;

    @CreationTimestamp
    @Column(name = "added_at", nullable = false, updatable = false)
    private Instant addedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
