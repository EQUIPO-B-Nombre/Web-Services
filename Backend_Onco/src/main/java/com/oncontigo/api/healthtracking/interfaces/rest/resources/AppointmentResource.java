package com.oncontigo.api.healthtracking.interfaces.rest.resources;

import java.time.LocalDateTime;

public record AppointmentResource(
        Long id,
        LocalDateTime dateTime,
        String description,
        Long healthTrackingId
) {
}
