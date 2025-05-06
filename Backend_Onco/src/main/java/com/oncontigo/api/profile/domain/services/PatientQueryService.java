package com.oncontigo.api.profile.domain.services;

import com.oncontigo.api.profile.domain.model.entities.Patient;
import com.oncontigo.api.profile.domain.model.queries.GetAllPatientsQuery;
import com.oncontigo.api.profile.domain.model.queries.GetPatientByIdQuery;
import com.oncontigo.api.profile.domain.model.queries.GetPatientByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface PatientQueryService {
    Optional<Patient> handle(GetPatientByIdQuery query);
    List<Patient> handle(GetAllPatientsQuery query);
    Optional<Patient> handle(GetPatientByUserIdQuery query);
}