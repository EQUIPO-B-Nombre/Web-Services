package com.oncontigo.api.healthtracking.domain.model.commands;

import java.time.LocalDateTime;

public record UpdateProcedureCommand(
        Long id,
        String name,
        String description,
        LocalDateTime performedAt
) {
}
