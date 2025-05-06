package com.oncontigo.api.healthtracking.domain.model.commands;

public record UpdatePrescriptionCommand(Long id,
                                        String medicationName,
                                        String dosage) {


}
