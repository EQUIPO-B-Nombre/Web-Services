package com.oncontigo.api.healthtracking.interfaces.rest.resources;

public record PrescriptionResource(
        Long id,
        Long patientId,
        Long doctorId,
        String medicationName,
        String dosage
) {
}