package com.oncontigo.api.healthtracking.interfaces.rest.resources;

import java.time.LocalDateTime;

public record UpdateAppointmentResource(
        String description,
        LocalDateTime dateTime
) {
}
