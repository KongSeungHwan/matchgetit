package com.matchgetit.backend.repository;

import com.matchgetit.backend.entity.DashboardDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DashboardDataRepository extends JpaRepository<DashboardDataEntity, Long> {
    @Query("SELECT canceledMembership FROM DashboardDataEntity")
    long findCanceledMembership();
}
