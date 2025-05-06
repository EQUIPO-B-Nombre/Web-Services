package com.oncontigo.api.healthtracking.interfaces.rest.resources;

import java.time.LocalDateTime;

public record UpdateProcedureResource(
        String name,
        String description,
        LocalDateTime performedAt
) {
}
