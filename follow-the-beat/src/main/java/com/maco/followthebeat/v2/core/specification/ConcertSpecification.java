package com.maco.followthebeat.v2.core.specification;

import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.entity.LineupEntry;
import com.maco.followthebeat.v2.core.entity.Stage;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class ConcertSpecification {

    public static Specification<Concert> hasArtistName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("artist").get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Concert> hasDate(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("schedule").get("date"), date);
    }

    public static Specification<Concert> hasFestivalId(UUID festivalId) {
        return (root, query, cb) -> {
            Path<Stage> stage = cb.treat(root.get("location"), Stage.class);
            return cb.equal(stage.get("festival").get("id"), festivalId);
        };
    }
}