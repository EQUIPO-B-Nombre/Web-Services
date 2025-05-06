package com.oncontigo.api.profile.domain.model.entities;

import com.oncontigo.api.iam.domain.model.aggregates.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;



    public Patient() {
    }

    public Patient(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return user.getId();
    }
}