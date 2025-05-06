package com.oncontigo.api.healthtracking.application.commandservices;

import com.oncontigo.api.healthtracking.application.outboundservices.acl.ExternalProfilesService;
import com.oncontigo.api.healthtracking.domain.exceptions.*;
import com.oncontigo.api.healthtracking.domain.model.commands.CreatePrescriptionCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.DeletePrescriptionCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdatePrescriptionCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Prescription;
import com.oncontigo.api.healthtracking.domain.services.PrescriptionCommandService;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.PrescriptionRepository;
import com.oncontigo.api.profile.domain.exceptions.DoctorNotFoundException;
import com.oncontigo.api.profile.domain.exceptions.PatientNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrescriptionCommandServiceImpl implements PrescriptionCommandService {

    private final PrescriptionRepository prescriptionRepository;
    private final ExternalProfilesService externalProfilesService;

    public PrescriptionCommandServiceImpl(PrescriptionRepository prescriptionRepository, ExternalProfilesService externalProfilesService) {
        this.prescriptionRepository = prescriptionRepository;
        this.externalProfilesService = externalProfilesService;
    }

    public Long handle(CreatePrescriptionCommand command) {
        var doctor = externalProfilesService.fetchDoctorById(command.doctorId());
        var patient = externalProfilesService.fetchPatientById(command.patientId());
        if (doctor.isEmpty()) throw new DoctorNotFoundException(command.doctorId());
        if (patient.isEmpty()) throw new PatientNotFoundException(command.patientId());
        Prescription prescription = new Prescription(command, patient.get(), doctor.get());
        prescriptionRepository.save(prescription);
        return prescription.getId();
    }

    public Optional<Prescription> handle(UpdatePrescriptionCommand command) {
        var prescription = prescriptionRepository.findById(command.id());
        if (prescription.isEmpty()) throw new PrescriptionNotFoundException(command.id());
        var updatedPrescription = prescription.get();
        prescriptionRepository.save(updatedPrescription.update(command));
        return Optional.of(updatedPrescription);
    }

    public void handle(DeletePrescriptionCommand command) {
        var prescription = prescriptionRepository.findById(command.id());
        if (prescription.isEmpty()) throw new PrescriptionNotFoundException(command.id());
        prescriptionRepository.delete(prescription.get());
    }

}