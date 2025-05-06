package com.oncontigo.api.profile.domain.services;

import com.oncontigo.api.profile.domain.model.entities.Doctor;
import com.oncontigo.api.profile.domain.model.queries.GetAllDoctorQuery;
import com.oncontigo.api.profile.domain.model.queries.GetDoctorByIdQuery;
import com.oncontigo.api.profile.domain.model.queries.GetDoctorByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface DoctorQueryService {
    Optional<Doctor> handle(GetDoctorByIdQuery query);
    List<Doctor> handle(GetAllDoctorQuery query);
    Optional<Doctor> handle(GetDoctorByUserIdQuery query);
}