package com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories;

import com.oncontigo.api.healthtracking.domain.model.entities.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatient_Id(Long patientId);
}