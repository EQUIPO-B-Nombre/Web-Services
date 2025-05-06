package com.oncontigo.api.healthtracking.domain.exceptions;

public class PrescriptionNotFoundException extends RuntimeException {
    public PrescriptionNotFoundException(Long prescriptionId) {
        super("Prescription with id " + prescriptionId + " not found");
    }
}