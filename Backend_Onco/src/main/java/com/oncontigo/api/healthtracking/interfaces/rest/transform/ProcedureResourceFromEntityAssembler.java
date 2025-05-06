package com.oncontigo.api.healthtracking.interfaces.rest.transform;

import com.oncontigo.api.healthtracking.domain.model.entities.Procedure;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.ProcedureResource;

public class ProcedureResourceFromEntityAssembler {

    public static ProcedureResource toResource(Procedure procedure) {
        return new ProcedureResource(
                procedure.getId(),
                procedure.getName(),
                procedure.getDescription(),
                procedure.getPerformedAt(),
                procedure.getHealthTrackingId()
        );
    }
}