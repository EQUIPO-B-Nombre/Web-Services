package com.oncontigo.api.healthtracking.interfaces.rest.transform;

import com.oncontigo.api.healthtracking.domain.model.commands.UpdateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.UpdateHealthTrackingResource;
import org.springframework.stereotype.Component;

@Component
public class UpdateHealthTrackingCommandFromResourceAssembler {

    public static UpdateHealthTrackingCommand toCommand(Long id, UpdateHealthTrackingResource resource) {
        return new UpdateHealthTrackingCommand(
                id,
                resource.status(),
                resource.description(),
                resource.lastVisit()
        );
    }
}