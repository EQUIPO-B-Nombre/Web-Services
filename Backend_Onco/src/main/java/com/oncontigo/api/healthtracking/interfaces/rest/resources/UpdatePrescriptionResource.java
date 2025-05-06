package com.oncontigo.api.healthtracking.interfaces.rest.resources;

public record UpdatePrescriptionResource(
        String medicationName,
        String dosage
) {
}