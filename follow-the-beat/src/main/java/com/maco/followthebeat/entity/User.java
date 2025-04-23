package com.maco.followthebeat.entity;

import com.maco.followthebeat.repo.SpotifyPlatformRepo;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "username", nullable = true, unique = true)
    private String username;
    @Column(name = "email", nullable = true, unique = true)
    private String email;
    @Column(name = "password_hash", nullable = true, unique = true)
    private String passwordHash;
    @Column(name = "is_anonymous", nullable = true)
    private boolean isAnonymous;
    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private SpotifyPlatform spotifyPlatform;
}
