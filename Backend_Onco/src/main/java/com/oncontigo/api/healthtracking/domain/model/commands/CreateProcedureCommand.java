package com.oncontigo.api.healthtracking.domain.model.commands;

import java.time.LocalDateTime;

public record CreateProcedureCommand(
        String name,
        String description,
        LocalDateTime performedAt,
        Long healthTrackingId
) {
}
