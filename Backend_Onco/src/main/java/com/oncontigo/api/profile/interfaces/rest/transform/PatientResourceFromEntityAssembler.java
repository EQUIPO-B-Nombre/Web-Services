package com.oncontigo.api.profile.interfaces.rest.transform;

import com.oncontigo.api.profile.domain.model.entities.Patient;
import com.oncontigo.api.profile.interfaces.rest.resources.PatientResource;

public class PatientResourceFromEntityAssembler {
    public static PatientResource toResourceFromEntity(Patient patient) {
        return new PatientResource(
                patient.getId(),
                patient.getUserId()
        );
    }
}