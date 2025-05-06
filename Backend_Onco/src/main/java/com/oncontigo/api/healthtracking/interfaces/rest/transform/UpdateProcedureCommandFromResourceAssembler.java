package com.oncontigo.api.healthtracking.interfaces.rest.transform;

import com.oncontigo.api.healthtracking.domain.model.commands.UpdateProcedureCommand;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.UpdateProcedureResource;
import org.springframework.stereotype.Component;

@Component
public class UpdateProcedureCommandFromResourceAssembler {

    public static UpdateProcedureCommand toCommand(Long id, UpdateProcedureResource resource) {
        return new UpdateProcedureCommand(
                id,
                resource.name(),
                resource.description(),
                resource.performedAt()
        );
    }
}