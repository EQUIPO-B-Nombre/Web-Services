package com.oncontigo.api.healthtracking.domain.exceptions;

public class HealthTrackingNotFoundException extends RuntimeException {
    public HealthTrackingNotFoundException(Long healthTrackingId) {
        super("HealthTracking con id " + healthTrackingId + " no encontrado");
    }
}