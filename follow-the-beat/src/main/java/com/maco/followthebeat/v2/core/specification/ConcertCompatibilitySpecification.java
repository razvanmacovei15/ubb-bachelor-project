package com.maco.followthebeat.v2.core.specification;

import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.entity.ConcertCompatibility;
import com.maco.followthebeat.v2.core.entity.LineupEntry;
import com.maco.followthebeat.v2.core.entity.Stage;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ConcertCompatibilitySpecification {
    public static Specification<ConcertCompatibility> hasUserId(UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<ConcertCompatibility> hasArtistName(String name) {
        return (root, query, cb) -> {
            Join<Object, Object> concertJoin = root.join("concert");
            Join<Object, Object> artistJoin = concertJoin.join("artist");
            return cb.like(cb.lower(artistJoin.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<ConcertCompatibility> hasCompatibilityGreaterThan(Float minCompatibility) {
        return (root, query, cb) -> cb.greaterThan(root.get("compatibility"), minCompatibility);
    }

    public static Specification<ConcertCompatibility> hasMinCompatibility(Float minCompatibility) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("compatibility"), minCompatibility);
    }

    public static Specification<ConcertCompatibility> hasFestivalId(UUID festivalId) {
        return (root, query, cb) -> {
            Path<Concert> concertPath = cb.treat(root.get("concert"), Concert.class);
            Path<Stage> stage = cb.treat(concertPath.get("location"), Stage.class);
            return cb.equal(stage.get("festival").get("id"), festivalId);
        };
    }
}
