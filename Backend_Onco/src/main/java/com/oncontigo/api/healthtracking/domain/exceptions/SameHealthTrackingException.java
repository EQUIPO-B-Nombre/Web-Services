package com.oncontigo.api.healthtracking.domain.exceptions;

public class SameHealthTrackingException extends RuntimeException {
    public SameHealthTrackingException(Long healthTrackingId) {
        super("HealthTracking with ID " + healthTrackingId + " already exists");
    }
}