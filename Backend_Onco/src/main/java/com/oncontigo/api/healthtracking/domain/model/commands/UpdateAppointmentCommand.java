package com.oncontigo.api.healthtracking.domain.model.commands;

import java.time.LocalDateTime;


public record UpdateAppointmentCommand(
        Long id,
        String description,
        LocalDateTime dateTime
) {
}
