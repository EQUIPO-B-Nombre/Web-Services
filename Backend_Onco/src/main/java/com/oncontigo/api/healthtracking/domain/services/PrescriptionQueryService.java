package com.oncontigo.api.healthtracking.domain.services;

import com.oncontigo.api.healthtracking.domain.model.queries.GetPrescriptionByIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAllPrescriptionsByPatientIdQuery;
import com.oncontigo.api.healthtracking.domain.model.entities.Prescription;

import java.util.List;
import java.util.Optional;

public interface PrescriptionQueryService {
    Optional<Prescription> handle(GetPrescriptionByIdQuery query);
    List<Prescription> handle(GetAllPrescriptionsByPatientIdQuery query);
}