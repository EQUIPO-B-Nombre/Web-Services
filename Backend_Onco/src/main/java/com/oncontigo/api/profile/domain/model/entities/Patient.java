package com.oncontigo.api.profile.domain.model.entities;

import com.oncontigo.api.iam.domain.model.aggregates.User;
import com.oncontigo.api.profile.domain.model.commands.CreatePatientCommand;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;



    public Patient() {
    }

    public Patient (CreatePatientCommand command, User user) {
        this.user = user;
    }

    public Patient(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return user.getId();
    }
}