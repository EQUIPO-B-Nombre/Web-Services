package com.oncontigo.api.profile.interfaces.acl;

import com.oncontigo.api.iam.domain.model.aggregates.User;
import com.oncontigo.api.profile.domain.model.aggregates.Profile;
import com.oncontigo.api.profile.domain.model.commands.CreateDoctorCommand;
import com.oncontigo.api.profile.domain.model.commands.CreatePatientCommand;
import com.oncontigo.api.profile.domain.model.commands.UpdateDoctorCommand;
import com.oncontigo.api.profile.domain.model.entities.Doctor;
import com.oncontigo.api.profile.domain.model.entities.Patient;
import com.oncontigo.api.profile.domain.model.queries.GetDoctorByIdQuery;
import com.oncontigo.api.profile.domain.model.queries.GetPatientByIdQuery;
import com.oncontigo.api.profile.domain.model.queries.GetProfileByUserIdQuery;
import com.oncontigo.api.profile.domain.services.DoctorCommandService;
import com.oncontigo.api.profile.domain.services.DoctorQueryService;
import com.oncontigo.api.profile.domain.services.PatientCommandService;
import com.oncontigo.api.profile.domain.services.PatientQueryService;
import com.oncontigo.api.profile.domain.services.ProfileQueryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProfilesContextFacade {
    private final ProfileQueryService profileQueryService;
    private final PatientQueryService patientQueryService;
    private final PatientCommandService patientCommandService;
    private final DoctorQueryService doctorQueryService;
    private final DoctorCommandService doctorCommandService;

    public ProfilesContextFacade(ProfileQueryService profileQueryService, PatientQueryService patientQueryService,
                                 PatientCommandService patientCommandService, DoctorQueryService doctorQueryService,
                                 DoctorCommandService doctorCommandService) {
        this.profileQueryService = profileQueryService;
        this.patientQueryService = patientQueryService;
        this.patientCommandService = patientCommandService;
        this.doctorQueryService = doctorQueryService;
        this.doctorCommandService = doctorCommandService;
    }

    public Optional<Profile> fetchProfileByPatientId(Long patientId) {
        var patientProfileQuery = new GetPatientByIdQuery(patientId);
        var patient = patientQueryService.handle(patientProfileQuery);
        if (patient.isEmpty()) return Optional.empty();
        Long userId = patient.get().getUserId();
        var profileQuery = new GetProfileByUserIdQuery(userId);
        return profileQueryService.handle(profileQuery);
    }

    public Optional<Profile> fetchProfileByDoctorId(Long dentistId) {
        var dentistProfileQuery = new GetDoctorByIdQuery(dentistId);
        var dentist = doctorQueryService.handle(dentistProfileQuery);
        if (dentist.isEmpty()) return Optional.empty();
        Long userId = dentist.get().getUserId();
        var profileQuery = new GetProfileByUserIdQuery(userId);
        return profileQueryService.handle(profileQuery);
    }

    public Optional<Patient> fetchPatientById(Long patientId) {
        var getPatientByIdQuery = new GetPatientByIdQuery(patientId);
        return patientQueryService.handle(getPatientByIdQuery);
    }

    public Optional<Doctor> fetchDoctorById(Long dentistId) {
        var getDentistByIdQuery = new GetDoctorByIdQuery(dentistId);
        return doctorQueryService.handle(getDentistByIdQuery);
    }

    public void updateRating(Long dentistId, BigDecimal rating) {
        var dentist = doctorQueryService.handle(new GetDoctorByIdQuery(dentistId));
        if (dentist.isEmpty()) return;
        doctorCommandService.handle(new UpdateDoctorCommand(dentist.get().getId(), rating));
    }

    public Long createPatient(Long userId, User user) {
        return patientCommandService.handle(new CreatePatientCommand(userId), user);
    }

    public Long createDentist(Long userId, User user) {
        return doctorCommandService.handle(new CreateDoctorCommand(userId), user);
    }
}