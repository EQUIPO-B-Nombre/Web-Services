package com.oncontigo.api.healthtracking.interfaces.rest.resources;

import java.time.LocalDateTime;

public record UpdateTreatmentResource(
        String name,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
