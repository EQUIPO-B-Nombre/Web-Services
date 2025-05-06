package com.oncontigo.api.profile.application.internal.outboundservices.queryservices;

import com.oncontigo.api.profile.domain.model.entities.Patient;
import com.oncontigo.api.profile.domain.model.queries.GetAllPatientsQuery;
import com.oncontigo.api.profile.domain.model.queries.GetPatientByIdQuery;
import com.oncontigo.api.profile.domain.model.queries.GetPatientByUserIdQuery;
import com.oncontigo.api.profile.domain.services.PatientQueryService;
import com.oncontigo.api.profile.infrastructure.persistence.jpa.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartientQueryServiceImpl implements PatientQueryService {
    private final PatientRepository patientRepository;

    public PartientQueryServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Optional<Patient> handle(GetPatientByIdQuery query) {
        return patientRepository.findById(query.id());
    }

    @Override
    public List<Patient> handle(GetAllPatientsQuery query) {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> handle(GetPatientByUserIdQuery query) {
        return patientRepository.findByUser_Id(query.userId());
    }
}