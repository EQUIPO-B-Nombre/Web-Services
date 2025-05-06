package com.oncontigo.api.healthtracking.application.queryservices;

import com.oncontigo.api.healthtracking.domain.model.entities.Procedure;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAllProceduresByHealthTrackingIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetProcedureByIdQuery;
import com.oncontigo.api.healthtracking.domain.services.ProcedureQueryService;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.ProcedureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProcedureQueryServiceImpl implements ProcedureQueryService {

    private final ProcedureRepository procedureRepository;

    public ProcedureQueryServiceImpl(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Override
    public Optional<Procedure> handle(GetProcedureByIdQuery query) {
        return procedureRepository.findById(query.id());
    }

    @Override
    public List<Procedure> handle(GetAllProceduresByHealthTrackingIdQuery query) {
        return procedureRepository.findByHealthTracking_Id(query.healthTrackingId());
    }
}