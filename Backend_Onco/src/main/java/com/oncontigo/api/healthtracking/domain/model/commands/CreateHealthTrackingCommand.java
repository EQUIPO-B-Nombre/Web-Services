package com.oncontigo.api.healthtracking.domain.model.commands;

import java.time.LocalDateTime;

public record CreateHealthTrackingCommand(
        Long patientId,
        Long doctorId,
        String status,
        String description,
        LocalDateTime lastVisit
) {}
