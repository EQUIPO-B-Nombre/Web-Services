package com.oncontigo.api.healthtracking.domain.services;

import com.oncontigo.api.healthtracking.domain.model.queries.GetTreatmentByIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAllTreatmentsByHealthTrackingIdQuery;
import com.oncontigo.api.healthtracking.domain.model.entities.Treatment;

import java.util.List;
import java.util.Optional;

public interface TreatmentQueryService {
    Optional<Treatment> handle(GetTreatmentByIdQuery query);
    List<Treatment> handle(GetAllTreatmentsByHealthTrackingIdQuery query);
}