package com.oncontigo.api.healthtracking.interfaces.rest.resources;

import java.time.LocalDateTime;

public record ProcedureResource(
        Long id,
        String name,
        String description,
        LocalDateTime performedAt,
        Long healthTrackingId
) {
}
