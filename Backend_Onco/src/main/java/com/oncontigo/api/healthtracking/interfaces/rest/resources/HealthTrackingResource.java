package com.oncontigo.api.healthtracking.interfaces.rest.resources;

import java.time.LocalDateTime;

public record HealthTrackingResource(
        Long id,
        Long patientId,
        Long doctorId,
        String status,
        String description,
        LocalDateTime lastVisit
) {
}
