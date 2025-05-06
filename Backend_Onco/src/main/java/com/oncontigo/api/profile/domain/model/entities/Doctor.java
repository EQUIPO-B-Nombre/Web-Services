package com.oncontigo.api.profile.domain.model.entities;

import com.oncontigo.api.iam.domain.model.aggregates.User;
import com.oncontigo.api.profile.domain.model.commands.UpdateDoctorCommand;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "Experience is required")
    @Column(precision = 3, scale = 1)
    private BigDecimal experience;

    public Doctor() {
        this.experience = BigDecimal.valueOf(0.0);
    }

    public Doctor(User user) {
        this.experience = BigDecimal.valueOf(0.0);
        this.user = user;
    }

    public Doctor update(UpdateDoctorCommand command) {
        this.experience = command.experience();
        return this;
    }

    public Long getUserId() {
        return user.getId();
    }
}