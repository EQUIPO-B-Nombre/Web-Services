package com.oncontigo.api.healthtracking.domain.model.commands;

import java.time.LocalDateTime;

public record UpdateTreatmentCommand(
        Long id,
        String name,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}