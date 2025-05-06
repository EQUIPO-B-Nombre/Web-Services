package com.oncontigo.api.profile.application.internal.outboundservices.commandservices;

import com.oncontigo.api.iam.domain.model.aggregates.User;
import com.oncontigo.api.profile.domain.exceptions.PatientNotFoundException;
import com.oncontigo.api.profile.domain.exceptions.SameUserException;
import com.oncontigo.api.profile.domain.model.commands.CreatePatientCommand;
import com.oncontigo.api.profile.domain.model.commands.DeletePatientCommand;
import com.oncontigo.api.profile.domain.model.entities.Patient;
import com.oncontigo.api.profile.domain.services.PatientCommandService;
import com.oncontigo.api.profile.infrastructure.persistence.jpa.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientCommandServiceImpl implements PatientCommandService {
    private final PatientRepository patientRepository;

    public PatientCommandServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Long handle(CreatePatientCommand command, User user) {
        var sameUser = patientRepository.findByUser_Id(command.userId());
        if (sameUser.isPresent()) {
            throw new SameUserException(command.userId());
        }
        Patient patient = new Patient(user);
        patientRepository.save(patient);
        return patient.getId();
    }

    @Override
    public void handle(DeletePatientCommand command) {
        var patient = patientRepository.findById(command.id());
        if (patient.isEmpty()) {
            throw new PatientNotFoundException(command.id());
        }
        patientRepository.delete(patient.get());
    }
}