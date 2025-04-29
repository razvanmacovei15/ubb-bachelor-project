package com.maco.followthebeat.feature.base.repo;

import com.maco.followthebeat.feature.base.entity.Schedule;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule, UUID> {
}
