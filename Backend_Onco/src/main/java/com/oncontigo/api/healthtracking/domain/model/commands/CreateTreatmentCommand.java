package com.oncontigo.api.healthtracking.domain.model.commands;

import java.time.LocalDateTime;

public record CreateTreatmentCommand(
        String name,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long healthTrackingId
) {
}