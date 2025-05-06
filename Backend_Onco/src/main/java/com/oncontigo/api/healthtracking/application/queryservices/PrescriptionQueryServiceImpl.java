package com.oncontigo.api.healthtracking.application.queryservices;

import com.oncontigo.api.healthtracking.domain.model.entities.Prescription;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAllPrescriptionsByPatientIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetPrescriptionByIdQuery;
import com.oncontigo.api.healthtracking.domain.services.PrescriptionQueryService;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionQueryServiceImpl implements PrescriptionQueryService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionQueryServiceImpl(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public Optional<Prescription> handle(GetPrescriptionByIdQuery query) {
        return prescriptionRepository.findById(query.id());
    }

    @Override
    public List<Prescription> handle(GetAllPrescriptionsByPatientIdQuery query) {
        return prescriptionRepository.findByPatient_Id(query.patientId());
    }
}