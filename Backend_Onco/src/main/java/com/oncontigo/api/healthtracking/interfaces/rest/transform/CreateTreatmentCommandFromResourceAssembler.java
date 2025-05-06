package com.oncontigo.api.healthtracking.interfaces.rest.transform;

import com.oncontigo.api.healthtracking.domain.model.commands.CreateTreatmentCommand;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.CreateTreatmentResource;

public class CreateTreatmentCommandFromResourceAssembler {

    public static CreateTreatmentCommand toCommand(CreateTreatmentResource resource) {
        return new CreateTreatmentCommand(
                resource.name(),
                resource.description(),
                resource.startDate(),
                resource.endDate(),
                resource.healthTrackingId()
        );
    }
}