package com.oncontigo.api.healthtracking.domain.model.entities;

import com.oncontigo.api.healthtracking.domain.model.commands.CreatePrescriptionCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdatePrescriptionCommand;
import com.oncontigo.api.profile.domain.model.entities.Doctor;
import com.oncontigo.api.profile.domain.model.entities.Patient;
import com.oncontigo.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
public class Prescription extends AuditableAbstractAggregateRoot<Prescription> {
    @NotNull(message = "Medication name is required")
    @NotBlank(message = "Medication name cannot be blank")
    private String medicationName;

    @NotNull(message = "Dosage is required")
    private String dosage;

    @NotNull(message = "Patient is required")
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @NotNull(message = "Prescribed by is required")
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    public Prescription() {
    }

    public Prescription(CreatePrescriptionCommand command, Patient patient, Doctor doctor) {
        this.medicationName = command.medicationName();
        this.dosage = command.dosage();
        this.patient = patient;
        this.doctor = doctor;
    }

    public Prescription update(UpdatePrescriptionCommand command) {
        this.medicationName = command.medicationName();
        this.dosage = command.dosage();
        return this;
    }

    public Long getPatientId() {
        return this.patient.getId();
    }

    public Long getDoctorId() {
        return this.doctor.getId();
    }




}