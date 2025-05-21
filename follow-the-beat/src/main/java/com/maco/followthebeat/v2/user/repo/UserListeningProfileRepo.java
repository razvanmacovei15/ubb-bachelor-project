package com.maco.followthebeat.v2.user.repo;

import com.maco.followthebeat.v2.user.entity.UserListeningProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserListeningProfileRepo extends JpaRepository<UserListeningProfile, UUID> {
    Optional<UserListeningProfile> findByUserId(UUID userId);


}
