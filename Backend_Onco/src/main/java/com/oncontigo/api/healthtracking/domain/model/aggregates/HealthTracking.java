package com.oncontigo.api.healthtracking.domain.model.aggregates;

import com.oncontigo.api.healthtracking.domain.model.commands.CreateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Appointment;
import com.oncontigo.api.healthtracking.domain.model.valueobjects.AppointmentStatus;
import com.oncontigo.api.healthtracking.domain.model.valueobjects.HealthTrackingStatus;
import com.oncontigo.api.profile.domain.model.entities.Doctor;
import com.oncontigo.api.profile.domain.model.entities.Patient;
import com.oncontigo.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class HealthTracking extends AuditableAbstractAggregateRoot<HealthTracking> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private HealthTrackingStatus status;

    @NotNull(message = "Description is required")
    private String description;


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
        this.status = HealthTrackingStatus.ACTIVE;
        this.description = command.description();
        this.lastVisit = null;
        this.patient = patient;
        this.doctor = doctor;
    }

    public HealthTracking update(UpdateHealthTrackingCommand command) {
        this.status = HealthTrackingStatus.valueOf(command.status());
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

    public void updateLastVisitIfCompleted(Appointment appointment) {
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            this.lastVisit = appointment.getDateTime();
        }
    }

}