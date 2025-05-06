package com.oncontigo.api.healthtracking.domain.model.entities;

import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateTreatmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateTreatmentCommand;
import com.oncontigo.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Treatment extends AuditableAbstractAggregateRoot<Treatment> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Treatment name is required")
    @NotBlank(message = "Treatment name cannot be blank")
    private String name;

    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @NotNull(message = "HealthTracking is required")
    @ManyToOne
    @JoinColumn(name = "health_tracking_id")
    private HealthTracking healthTracking;

    public Treatment() {
    }

    public Treatment(CreateTreatmentCommand command, HealthTracking healthTracking) {
        this.name = command.name();
        this.description = command.description();
        this.startDate = command.startDate();
        this.endDate = command.endDate();
        this.healthTracking = healthTracking;
    }

    public Treatment update(UpdateTreatmentCommand command) {
        this.name = command.name();
        this.description = command.description();
        this.startDate = command.startDate();
        this.endDate = command.endDate();
        return this;
    }

    public Long getHealthTrackingId() {
        return this.healthTracking.getId();
    }
}