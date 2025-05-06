package com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories;

import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HealthTrackingRepository extends JpaRepository<HealthTracking, Long> {
    Optional<HealthTracking> findByPatient_Id(Long patientId);
    List<HealthTracking> findByDoctor_Id(Long doctorId);
}