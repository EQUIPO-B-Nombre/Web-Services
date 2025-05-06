package com.oncontigo.api.profile.domain.services;

import com.oncontigo.api.iam.domain.model.aggregates.User;
import com.oncontigo.api.profile.domain.model.commands.CreatePatientCommand;
import com.oncontigo.api.profile.domain.model.commands.DeletePatientCommand;


import java.util.Optional;

public interface PatientCommandService {
    Long handle(CreatePatientCommand command, User user);
    void handle(DeletePatientCommand command);
}