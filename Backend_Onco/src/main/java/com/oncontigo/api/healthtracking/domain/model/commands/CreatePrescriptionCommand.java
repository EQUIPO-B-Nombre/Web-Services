package com.oncontigo.api.healthtracking.domain.model.commands;

public record CreatePrescriptionCommand(Long patientId,
                                        Long doctorId,
                                        String medicationName,
                                        String dosage) {
}
