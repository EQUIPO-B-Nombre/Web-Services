package com.oncontigo.api.healthtracking.application.outboundservices.acl;

import com.oncontigo.api.profile.domain.model.entities.Doctor;
import com.oncontigo.api.profile.domain.model.entities.Patient;
import com.oncontigo.api.profile.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExternalProfilesService {
    private final ProfilesContextFacade profilesContextFacade;

    public ExternalProfilesService(ProfilesContextFacade profilesContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
    }

    public Optional<Doctor> fetchDoctorById(Long doctorId) {
        return profilesContextFacade.fetchDoctorById(doctorId);
    }

    public Optional<Patient> fetchPatientById(Long patientId) {
        return profilesContextFacade.fetchPatientById(patientId);
    }

}