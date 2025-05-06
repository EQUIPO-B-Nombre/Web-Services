package com.oncontigo.api.healthtracking.interfaces.rest.transform;

import com.oncontigo.api.healthtracking.domain.model.commands.UpdateTreatmentCommand;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.UpdateTreatmentResource;
import org.springframework.stereotype.Component;

@Component
public class UpdateTreatmentCommandFromResourceAssembler {

    public static UpdateTreatmentCommand toCommand(Long id, UpdateTreatmentResource resource) {
        return new UpdateTreatmentCommand(
                id,
                resource.name(),
                resource.description(),
                resource.startDate(),
                resource.endDate()
        );
    }
}