package com.oncontigo.api.healthtracking.interfaces.rest.transform;

import com.oncontigo.api.healthtracking.domain.model.commands.UpdateAppointmentCommand;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.UpdateAppointmentResource;
import org.springframework.stereotype.Component;

@Component
public class UpdateAppointmentCommandFromResourceAssembler {

    public static UpdateAppointmentCommand toCommand(Long id, UpdateAppointmentResource resource) {
        return new UpdateAppointmentCommand(
                id,
                resource.description(),
                resource.dateTime()
        );
    }
}