package com.oncontigo.api.healthtracking.interfaces.rest.transform;

import com.oncontigo.api.healthtracking.domain.model.commands.CreateProcedureCommand;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.CreateProcedureResource;

public class CreateProcedureCommandFromResourceAssembler {

    public static CreateProcedureCommand toCommand(CreateProcedureResource resource) {
        return new CreateProcedureCommand(
                resource.name(),
                resource.description(),
                resource.performedAt(),
                resource.healthTrackingId()
        );
    }
}