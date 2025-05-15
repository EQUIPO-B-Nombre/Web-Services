package com.oncontigo.api.healthtracking.domain.exceptions;

public class DuplicateHealthTrackingException extends RuntimeException {
    public DuplicateHealthTrackingException(Long patientId, Long doctorId) {
        super(String.format("A HealthTracking already exists for Patient ID %d and Doctor ID %d", patientId, doctorId));
    }
}