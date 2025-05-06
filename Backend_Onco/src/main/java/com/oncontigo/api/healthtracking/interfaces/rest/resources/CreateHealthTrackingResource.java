package com.oncontigo.api.healthtracking.interfaces.rest.resources;

import java.time.LocalDateTime;

public record CreateHealthTrackingResource(
        Long patientId,
        Long doctorId,
        String status,
        String description,
        LocalDateTime lastVisit
) {
}
