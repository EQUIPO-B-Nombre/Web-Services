package com.oncontigo.api.healthtracking.application.commandservices;

import com.oncontigo.api.healthtracking.domain.exceptions.TreatmentNotFoundException;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateTreatmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.DeleteTreatmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateTreatmentCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Treatment;
import com.oncontigo.api.healthtracking.domain.services.TreatmentCommandService;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.HealthTrackingRepository;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.TreatmentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TreatmentCommandServiceImpl implements TreatmentCommandService {

    private final TreatmentRepository treatmentRepository;
    private final HealthTrackingRepository healthTrackingRepository;

    public TreatmentCommandServiceImpl(TreatmentRepository treatmentRepository, HealthTrackingRepository healthTrackingRepository) {
        this.treatmentRepository = treatmentRepository;
        this.healthTrackingRepository = healthTrackingRepository;
    }

    @Override
    public Long handle(CreateTreatmentCommand command) {
        var healthTracking = healthTrackingRepository.findById(command.healthTrackingId());
        if (healthTracking.isEmpty()) throw new IllegalArgumentException("HealthTracking not found with ID: " + command.healthTrackingId());
        Treatment treatment = new Treatment(command, healthTracking.get());
        treatmentRepository.save(treatment);
        return treatment.getId();
    }

    @Override
    public Optional<Treatment> handle(UpdateTreatmentCommand command) {
        var treatment = treatmentRepository.findById(command.id());
        if (treatment.isEmpty()) throw new TreatmentNotFoundException(command.id());
        var updatedTreatment = treatment.get();
        treatmentRepository.save(updatedTreatment.update(command));
        return Optional.of(updatedTreatment);
    }

    @Override
    public void handle(DeleteTreatmentCommand command) {
        var treatment = treatmentRepository.findById(command.id());
        if (treatment.isEmpty()) throw new TreatmentNotFoundException(command.id());
        treatmentRepository.delete(treatment.get());
    }
}