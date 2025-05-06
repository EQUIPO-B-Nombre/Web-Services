package com.oncontigo.api.healthtracking.domain.model.commands;

import java.time.LocalDateTime;

public record CreateAppointmentCommand(
        Long healthTrackingId,
        String description,
        LocalDateTime dateTime
) {
}
