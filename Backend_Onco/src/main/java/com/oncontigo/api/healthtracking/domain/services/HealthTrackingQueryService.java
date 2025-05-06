package com.oncontigo.api.healthtracking.domain.services;

import com.oncontigo.api.healthtracking.domain.model.queries.GetAllHealthTrackingsByDoctorIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetHealthTrackingByIdQuery;
import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.queries.GetHealthTrackingByPatientIdQuery;

import java.util.List;
import java.util.Optional;

public interface HealthTrackingQueryService {
    Optional<HealthTracking> handle(GetHealthTrackingByIdQuery query);
    Optional<HealthTracking> handle(GetHealthTrackingByPatientIdQuery query);
    List<HealthTracking> handle(GetAllHealthTrackingsByDoctorIdQuery query);
}