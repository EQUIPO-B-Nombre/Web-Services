package com.oncontigo.api.healthtracking.interfaces.rest.resources;

import java.time.LocalDateTime;

public record UpdateHealthTrackingResource(
        String status,
        String description,
        LocalDateTime lastVisit
) {
}
