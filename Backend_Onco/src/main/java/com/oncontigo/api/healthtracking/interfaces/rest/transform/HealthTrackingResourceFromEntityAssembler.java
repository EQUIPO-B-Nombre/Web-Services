package com.oncontigo.api.healthtracking.interfaces.rest.transform;

import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.HealthTrackingResource;

public class HealthTrackingResourceFromEntityAssembler {

    public static HealthTrackingResource toResource(HealthTracking healthTracking) {
        return new HealthTrackingResource(
                healthTracking.getId(),
                healthTracking.getPatientId(),
                healthTracking.getDoctorId(),
                healthTracking.getStatus(),
                healthTracking.getDescription(),
                healthTracking.getLastVisit()
        );
    }
}