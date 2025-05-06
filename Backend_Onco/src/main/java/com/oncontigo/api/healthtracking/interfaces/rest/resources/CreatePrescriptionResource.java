package com.oncontigo.api.healthtracking.interfaces.rest.resources;

public record CreatePrescriptionResource(
        Long patientId,
        Long doctorId,
        String medicationName,
        String dosage
) {
}