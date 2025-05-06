package com.oncontigo.api.healthtracking.application.queryservices;

import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAllHealthTrackingsByDoctorIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetHealthTrackingByIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetHealthTrackingByPatientIdQuery;
import com.oncontigo.api.healthtracking.domain.services.HealthTrackingQueryService;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.HealthTrackingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HealthTrackingQueryServiceImpl implements HealthTrackingQueryService {

    private final HealthTrackingRepository healthTrackingRepository;

    public HealthTrackingQueryServiceImpl(HealthTrackingRepository healthTrackingRepository) {
        this.healthTrackingRepository = healthTrackingRepository;
    }

    @Override
    public Optional<HealthTracking> handle(GetHealthTrackingByIdQuery query) {
        return healthTrackingRepository.findById(query.id());
    }

    @Override
    public List<HealthTracking> handle(GetAllHealthTrackingsByDoctorIdQuery query) {
        return healthTrackingRepository.findByDoctor_Id(query.doctorId());
    }

    @Override
    public Optional<HealthTracking> handle(GetHealthTrackingByPatientIdQuery query) {
        return healthTrackingRepository.findByPatient_Id(query.patientId());
    }
}