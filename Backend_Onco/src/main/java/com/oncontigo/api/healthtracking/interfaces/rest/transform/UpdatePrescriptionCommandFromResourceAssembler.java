package com.oncontigo.api.healthtracking.interfaces.rest.transform;

import com.oncontigo.api.healthtracking.domain.model.commands.UpdatePrescriptionCommand;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.UpdatePrescriptionResource;
import org.springframework.stereotype.Component;

@Component
public class UpdatePrescriptionCommandFromResourceAssembler {

    public static UpdatePrescriptionCommand toCommand(Long id,UpdatePrescriptionResource resource) {
        return new UpdatePrescriptionCommand(
                id,
                resource.medicationName(),
                resource.dosage()
        );
    }
}