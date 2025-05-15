package com.oncontigo.api.healthtracking.domain.model.entities;

import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateAppointmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateAppointmentCommand;
import com.oncontigo.api.healthtracking.domain.model.valueobjects.AppointmentStatus;
import com.oncontigo.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Appointment extends AuditableAbstractAggregateRoot<Appointment> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "HealthTracking is required")
    @ManyToOne
    @JoinColumn(name = "health_tracking_id")
    private HealthTracking healthTracking;

    @Setter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private AppointmentStatus status;

    @NotNull(message = "Date and time are required")
    private LocalDateTime dateTime;

    private String description;

    public Appointment() {
    }

    public Appointment(CreateAppointmentCommand command, HealthTracking healthTracking) {
        this.healthTracking = healthTracking;
        this.dateTime = command.dateTime();
        this.description = command.description();
        this.status = AppointmentStatus.PENDING;
    }

    public Appointment update(UpdateAppointmentCommand command) {
        this.dateTime = command.dateTime();
        this.description = command.description();
        this.status = AppointmentStatus.valueOf(command.status());
        return this;
    }


    public Long getHealthTrackingId() {
        return this.healthTracking.getId();
    }
}