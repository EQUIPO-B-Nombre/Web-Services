package com.oncontigo.api.healthtracking.domain.exceptions;

public class TreatmentNotFoundException extends RuntimeException {
    public TreatmentNotFoundException(Long treatmentId) {
        super("Treatment with id " + treatmentId + " not found");
    }
}