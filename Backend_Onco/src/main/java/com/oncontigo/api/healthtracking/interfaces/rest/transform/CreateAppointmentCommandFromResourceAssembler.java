package com.oncontigo.api.healthtracking.interfaces.rest.transform;

import com.oncontigo.api.healthtracking.domain.model.commands.CreateAppointmentCommand;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.CreateAppointmentResource;

public class CreateAppointmentCommandFromResourceAssembler {

    public static CreateAppointmentCommand toCommand(CreateAppointmentResource resource) {
        return new CreateAppointmentCommand(
                resource.healthTrackingId(),
                resource.description(),
                resource.dateTime()
        );
    }
}