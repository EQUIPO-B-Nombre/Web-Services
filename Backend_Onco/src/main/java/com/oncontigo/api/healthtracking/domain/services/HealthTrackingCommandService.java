package com.oncontigo.api.healthtracking.domain.services;

import com.oncontigo.api.healthtracking.domain.model.commands.CreateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.DeleteHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;

import java.util.Optional;

public interface HealthTrackingCommandService {
    Long handle(CreateHealthTrackingCommand command);
    Optional<HealthTracking> handle(UpdateHealthTrackingCommand command);
    void handle(DeleteHealthTrackingCommand command);
}