package com.oncontigo.api.healthtracking.domain.services;

import com.oncontigo.api.healthtracking.domain.model.queries.GetProcedureByIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAllProceduresByHealthTrackingIdQuery;
import com.oncontigo.api.healthtracking.domain.model.entities.Procedure;

import java.util.List;
import java.util.Optional;

public interface ProcedureQueryService {
    Optional<Procedure> handle(GetProcedureByIdQuery query);
    List<Procedure> handle(GetAllProceduresByHealthTrackingIdQuery query);
}