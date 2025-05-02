package com.maco.followthebeat.v2.core.repo;

import com.maco.followthebeat.v2.core.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StageRepo extends JpaRepository<Stage, UUID> {
    @Query("SELECT s FROM Stage s WHERE s.name = :name AND s.festival.id = :festivalId")
    Optional<Stage> findByNameAndFestivalId(@Param("name") String name, @Param("festivalId") UUID festivalId);

}
