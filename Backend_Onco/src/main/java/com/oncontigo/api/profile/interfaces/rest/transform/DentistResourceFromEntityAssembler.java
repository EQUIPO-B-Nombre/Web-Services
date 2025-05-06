package com.oncontigo.api.profile.interfaces.rest.transform;

import com.oncontigo.api.profile.domain.model.entities.Doctor;
import com.oncontigo.api.profile.interfaces.rest.resources.DoctorResource;

public class DentistResourceFromEntityAssembler {
    public static DoctorResource toResourceFromEntity(Doctor doctor) {
        return new DoctorResource(
                doctor.getId(),
                doctor.getUserId(),
                doctor.getExperience()
        );
    }
}