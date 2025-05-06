package com.oncontigo.api.profile.domain.services;

import com.oncontigo.api.iam.domain.model.aggregates.User;
import com.oncontigo.api.profile.domain.model.commands.CreateDoctorCommand;
import com.oncontigo.api.profile.domain.model.commands.DeleteDoctorCommand;
import com.oncontigo.api.profile.domain.model.commands.UpdateDoctorCommand;
import com.oncontigo.api.profile.domain.model.entities.Doctor;

import java.util.Optional;

public interface DoctorCommandService {
    Long handle(CreateDoctorCommand command, User user);
    Optional<Doctor> handle(UpdateDoctorCommand command);
    void handle(DeleteDoctorCommand command);
}