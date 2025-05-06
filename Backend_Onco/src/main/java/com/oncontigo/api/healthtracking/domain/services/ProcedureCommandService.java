package com.oncontigo.api.healthtracking.domain.services;

import com.oncontigo.api.healthtracking.domain.model.commands.CreateProcedureCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.DeleteProcedureCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateProcedureCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Procedure;

import java.util.Optional;

public interface ProcedureCommandService {
    Long handle(CreateProcedureCommand command);
    Optional<Procedure> handle(UpdateProcedureCommand command);
    void handle(DeleteProcedureCommand command);
}