package com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories;

import com.oncontigo.api.healthtracking.domain.model.entities.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
    List<Treatment> findByHealthTracking_Id(Long healthTrackingId);
}