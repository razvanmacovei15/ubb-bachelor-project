package com.maco.followthebeat.v2.core.specification;

import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.entity.LineupEntry;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.UUID;

public class LineupEntrySpecification {

    public static Specification<LineupEntry> hasArtistName(String name) {
        return (root, query, cb) -> {
            Join<Object, Object> concertJoin = root.join("concert");
            Join<Object, Object> artistJoin = concertJoin.join("artist");
            return cb.like(cb.lower(artistJoin.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<LineupEntry> hasUserId(UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<LineupEntry> hasConcertId(UUID concertId) {
        return (root, query, cb) -> cb.equal(root.get("concert").get("id"), concertId);
    }

    public static Specification<LineupEntry> hasPriority(Integer priority) {
        return (root, query, cb) -> cb.equal(root.get("priority"), priority);
    }

    public static Specification<LineupEntry> hasPriorityGreaterThan(Integer minPriority) {
        return (root, query, cb) -> cb.greaterThan(root.get("priority"), minPriority);
    }

    public static Specification<LineupEntry> hasCompatibilityGreaterThan(Integer minCompatibility) {
        return (root, query, cb) -> cb.greaterThan(root.get("compatibility"), minCompatibility);
    }

    public static Specification<LineupEntry> hasMinPriority(Integer minPriority) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("priority"), minPriority);
    }

    public static Specification<LineupEntry> hasMinCompatibility(Integer minCompatibility) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("compatibility"), minCompatibility);
    }

    public static Specification<LineupEntry> addedAfter(Instant addedAfter) {
        return (root, query, cb) -> cb.greaterThan(root.get("createdAt"), addedAfter);
    }

    public static Specification<LineupEntry> updatedAfter(Instant updatedAfter) {
        return (root, query, cb) -> cb.greaterThan(root.get("updatedAt"), updatedAfter);
    }
}
