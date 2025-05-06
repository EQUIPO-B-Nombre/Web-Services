package com.oncontigo.api.healthtracking.application.queryservices;

import com.oncontigo.api.healthtracking.domain.model.entities.Treatment;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAllTreatmentsByHealthTrackingIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetTreatmentByIdQuery;
import com.oncontigo.api.healthtracking.domain.services.TreatmentQueryService;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.TreatmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TreatmentQueryServiceImpl implements TreatmentQueryService {

    private final TreatmentRepository treatmentRepository;

    public TreatmentQueryServiceImpl(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    @Override
    public Optional<Treatment> handle(GetTreatmentByIdQuery query) {
        return treatmentRepository.findById(query.id());
    }

    @Override
    public List<Treatment> handle(GetAllTreatmentsByHealthTrackingIdQuery query) {
        return treatmentRepository.findByHealthTracking_Id(query.healthTrackingId());
    }
}