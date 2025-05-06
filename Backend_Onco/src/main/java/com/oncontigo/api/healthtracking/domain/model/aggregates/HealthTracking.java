package com.oncontigo.api.healthtracking.domain.model.aggregates;

import com.oncontigo.api.healthtracking.domain.model.commands.CreateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateHealthTrackingCommand;
import com.oncontigo.api.profile.domain.model.entities.Doctor;
import com.oncontigo.api.profile.domain.model.entities.Patient;
import com.oncontigo.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class HealthTracking extends AuditableAbstractAggregateRoot<HealthTracking> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Status is required")
    @NotBlank(message = "Status cannot be blank")
    private String status;

    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "Last visit is required")
    private LocalDateTime lastVisit;

    @NotNull(message = "Patient is required")
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @NotNull(message = "Doctor is required")
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    public HealthTracking() {
    }

    public HealthTracking(CreateHealthTrackingCommand command, Patient patient, Doctor doctor) {
        this.status = command.status();
        this.description = command.description();
        this.lastVisit = command.lastVisit();
        this.patient = patient;
        this.doctor = doctor;
    }

    public HealthTracking update(UpdateHealthTrackingCommand command) {
        this.status = command.status();
        this.description = command.description();
        this.lastVisit = command.lastVisit();
        return this;
    }

    public Long getPatientId() {
        return this.patient.getId();
    }

    public Long getDoctorId() {
        return this.doctor.getId();
    }
}