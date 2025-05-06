package com.oncontigo.api.profile.infrastructure.persistence.jpa;

import com.oncontigo.api.profile.domain.model.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser_Id(Long userId);
}