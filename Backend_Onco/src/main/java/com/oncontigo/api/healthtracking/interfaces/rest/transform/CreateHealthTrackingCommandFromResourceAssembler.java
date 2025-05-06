package com.oncontigo.api.healthtracking.interfaces.rest.transform;

import com.oncontigo.api.healthtracking.domain.model.commands.CreateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.CreateHealthTrackingResource;

public class CreateHealthTrackingCommandFromResourceAssembler {

    public static CreateHealthTrackingCommand toCommand(CreateHealthTrackingResource resource) {
        return new CreateHealthTrackingCommand(
                resource.patientId(),
                resource.doctorId(),
                resource.status(),
                resource.description(),
                resource.lastVisit()
        );
    }
}