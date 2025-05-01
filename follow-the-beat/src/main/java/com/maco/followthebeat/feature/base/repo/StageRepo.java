package com.maco.followthebeat.feature.base.repo;

import com.maco.followthebeat.feature.base.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StageRepo extends JpaRepository<Stage, UUID> {
    Optional<Stage> findByNameAndFestival_Id(String name, UUID festivalId);

}
