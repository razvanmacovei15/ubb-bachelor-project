package com.maco.followthebeat.v2.core.specification;

import com.maco.followthebeat.v2.core.entity.Concert;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ConcertSpecification {

    public static Specification<Concert> hasArtistName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("artist").get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Concert> hasCity(String city) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("location").get("city")), "%" + city.toLowerCase() + "%");
    }

    public static Specification<Concert> hasDate(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("schedule").get("date"), date);
    }
}