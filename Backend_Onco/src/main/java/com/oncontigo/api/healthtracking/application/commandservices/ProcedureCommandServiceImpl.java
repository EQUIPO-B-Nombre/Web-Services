package com.oncontigo.api.healthtracking.application.commandservices;

import com.oncontigo.api.healthtracking.domain.exceptions.ProcedureNotFoundException;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateProcedureCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.DeleteProcedureCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateProcedureCommand;
import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.entities.Procedure;
import com.oncontigo.api.healthtracking.domain.services.ProcedureCommandService;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.HealthTrackingRepository;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.ProcedureRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProcedureCommandServiceImpl implements ProcedureCommandService {

    private final ProcedureRepository procedureRepository;
    private final HealthTrackingRepository healthTrackingRepository;

    public ProcedureCommandServiceImpl(ProcedureRepository procedureRepository, HealthTrackingRepository healthTrackingRepository) {
        this.procedureRepository = procedureRepository;
        this.healthTrackingRepository = healthTrackingRepository;
    }

    @Override
    public Long handle(CreateProcedureCommand command) {
        var healthTracking = healthTrackingRepository.findById(command.healthTrackingId());
        if (healthTracking.isEmpty()) throw new IllegalArgumentException("HealthTracking not found with ID: " + command.healthTrackingId());
        Procedure procedure = new Procedure(command, healthTracking.get());
        procedureRepository.save(procedure);
        return procedure.getId();
    }

    @Override
    public Optional<Procedure> handle(UpdateProcedureCommand command) {
        var procedure = procedureRepository.findById(command.id());
        if (procedure.isEmpty()) throw new ProcedureNotFoundException(command.id());
        var updatedProcedure = procedure.get();
        procedureRepository.save(updatedProcedure.update(command));
        return Optional.of(updatedProcedure);
    }

    @Override
    public void handle(DeleteProcedureCommand command) {
        var procedure = procedureRepository.findById(command.id());
        if (procedure.isEmpty()) throw new ProcedureNotFoundException(command.id());
        procedureRepository.delete(procedure.get());
    }
}