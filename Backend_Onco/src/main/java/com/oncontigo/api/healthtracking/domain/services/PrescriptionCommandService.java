package com.oncontigo.api.healthtracking.domain.services;



import com.oncontigo.api.healthtracking.domain.model.commands.CreatePrescriptionCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.DeletePrescriptionCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdatePrescriptionCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Prescription;

import java.util.Optional;

public interface PrescriptionCommandService {
    Long handle(CreatePrescriptionCommand command);
    Optional<Prescription> handle(UpdatePrescriptionCommand command);
    void handle(DeletePrescriptionCommand command);
}