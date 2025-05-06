package com.oncontigo.api.profile.infrastructure.persistence.jpa;

import com.oncontigo.api.profile.domain.model.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser_Id(Long userId);
}