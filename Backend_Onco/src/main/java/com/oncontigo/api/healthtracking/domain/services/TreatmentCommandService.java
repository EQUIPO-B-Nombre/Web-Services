package com.oncontigo.api.healthtracking.domain.services;

import com.oncontigo.api.healthtracking.domain.model.commands.CreateTreatmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.DeleteTreatmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateTreatmentCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Treatment;

import java.util.Optional;

public interface TreatmentCommandService {
    Long handle(CreateTreatmentCommand command);
    Optional<Treatment> handle(UpdateTreatmentCommand command);
    void handle(DeleteTreatmentCommand command);
}