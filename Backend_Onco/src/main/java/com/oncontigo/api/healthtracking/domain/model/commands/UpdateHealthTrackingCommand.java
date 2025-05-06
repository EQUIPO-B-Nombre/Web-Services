package com.oncontigo.api.healthtracking.domain.model.commands;

import java.time.LocalDateTime;


public record UpdateHealthTrackingCommand(
        Long id,
        String status,
        String description,
        LocalDateTime lastVisit
) {}
