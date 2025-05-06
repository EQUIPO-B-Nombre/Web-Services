package com.oncontigo.api.healthtracking.domain.model.entities;

import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateProcedureCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateProcedureCommand;
import com.oncontigo.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Procedure extends AuditableAbstractAggregateRoot<Procedure> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Procedure name is required")
    @NotBlank(message = "Procedure name cannot be blank")
    private String name;

    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "Performed date is required")
    private LocalDateTime performedAt;

    @NotNull(message = "HealthTracking is required")
    @ManyToOne
    @JoinColumn(name = "health_tracking_id")
    private HealthTracking healthTracking;

    public Procedure() {
    }

    public Procedure(CreateProcedureCommand command, HealthTracking healthTracking) {
        this.name = command.name();
        this.description = command.description();
        this.performedAt = command.performedAt();
        this.healthTracking = healthTracking;
    }

    public Procedure update(UpdateProcedureCommand command) {
        this.name = command.name();
        this.description = command.description();
        this.performedAt = command.performedAt();
        return this;
    }

    public Long getHealthTrackingId() {
        return this.healthTracking.getId();
    }
}