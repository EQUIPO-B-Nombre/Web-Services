package com.oncontigo.api.healthtracking.interfaces.rest.transform;

import com.oncontigo.api.healthtracking.domain.model.entities.Treatment;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.TreatmentResource;

public class TreatmentResourceFromEntityAssembler {

    public static TreatmentResource toResource(Treatment treatment) {
        return new TreatmentResource(
                treatment.getId(),
                treatment.getName(),
                treatment.getDescription(),
                treatment.getStartDate(),
                treatment.getEndDate(),
                treatment.getHealthTrackingId()
        );
    }
}