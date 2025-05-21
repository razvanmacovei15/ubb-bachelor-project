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
            Join<Object, Object> concertCompatibility = root.join("concertCompatibility");
            Join<Object, Object> concert = concertCompatibility.join("concert");
            Join<Object, Object> artist = concert.join("artist");
            return cb.like(cb.lower(artist.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<LineupEntry> hasUserId(UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("concertCompatibility").get("user").get("id"), userId);
    }

}
