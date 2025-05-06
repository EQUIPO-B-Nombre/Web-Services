package com.oncontigo.api.healthtracking.interfaces.rest.resources;

import java.time.LocalDateTime;

public record CreateAppointmentResource(
        String description,
        Long healthTrackingId,
        LocalDateTime dateTime
) {
}
