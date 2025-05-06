package com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories;

import com.oncontigo.api.healthtracking.domain.model.entities.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcedureRepository extends JpaRepository<Procedure, Long> {
    List<Procedure> findByHealthTracking_Id(Long healthTrackingId);
}