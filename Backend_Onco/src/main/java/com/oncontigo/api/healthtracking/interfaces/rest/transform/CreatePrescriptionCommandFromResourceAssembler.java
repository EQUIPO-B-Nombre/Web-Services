package com.oncontigo.api.healthtracking.interfaces.rest.transform;

import com.oncontigo.api.healthtracking.domain.model.commands.CreatePrescriptionCommand;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.CreatePrescriptionResource;

public class CreatePrescriptionCommandFromResourceAssembler {

    public static CreatePrescriptionCommand toCommand(CreatePrescriptionResource resource) {
        return new CreatePrescriptionCommand(
                resource.patientId(),
                resource.doctorId(),
                resource.medicationName(),
                resource.dosage()
        );
    }
}