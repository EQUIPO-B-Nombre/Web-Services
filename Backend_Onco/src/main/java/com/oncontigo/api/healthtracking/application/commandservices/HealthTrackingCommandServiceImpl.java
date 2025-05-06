package com.oncontigo.api.healthtracking.application.commandservices;

import com.oncontigo.api.healthtracking.application.outboundservices.acl.ExternalProfilesService;
import com.oncontigo.api.healthtracking.domain.exceptions.*;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.DeleteHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.services.HealthTrackingCommandService;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.HealthTrackingRepository;
import com.oncontigo.api.profile.domain.exceptions.DoctorNotFoundException;
import com.oncontigo.api.profile.domain.exceptions.PatientNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HealthTrackingCommandServiceImpl implements HealthTrackingCommandService {

    private final HealthTrackingRepository healthTrackingRepository;
    private final ExternalProfilesService externalProfilesService;

    public HealthTrackingCommandServiceImpl(HealthTrackingRepository healthTrackingRepository, ExternalProfilesService externalProfilesService) {
        this.healthTrackingRepository = healthTrackingRepository;
        this.externalProfilesService = externalProfilesService;
    }

    public Long handle(CreateHealthTrackingCommand command) {
        var doctor = externalProfilesService.fetchDoctorById(command.doctorId());
        var patient = externalProfilesService.fetchPatientById(command.patientId());
        if (doctor.isEmpty()) throw new DoctorNotFoundException(command.doctorId());
        if (patient.isEmpty()) throw new PatientNotFoundException(command.patientId());
        HealthTracking healthTracking = new HealthTracking(command, patient.get(), doctor.get());
        healthTrackingRepository.save(healthTracking);
        return healthTracking.getId();
    }

    public Optional<HealthTracking> handle(UpdateHealthTrackingCommand command) {
        var healthTracking = healthTrackingRepository.findById(command.id());
        if (healthTracking.isEmpty()) throw new HealthTrackingNotFoundException(command.id());
        var updatedHealthTracking = healthTracking.get();
        healthTrackingRepository.save(updatedHealthTracking.update(command));
        return Optional.of(updatedHealthTracking);
    }

    public void handle(DeleteHealthTrackingCommand command) {
        var healthTracking = healthTrackingRepository.findById(command.id());
        if (healthTracking.isEmpty()) throw new HealthTrackingNotFoundException(command.id());
        healthTrackingRepository.delete(healthTracking.get());
    }

}