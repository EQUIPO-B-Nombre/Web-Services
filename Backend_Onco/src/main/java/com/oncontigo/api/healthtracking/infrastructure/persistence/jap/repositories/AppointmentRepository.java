package com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories;

import com.oncontigo.api.healthtracking.domain.model.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByHealthTracking_Id(Long healthTrackingId);
}