package com.oncontigo.api.profile.application.internal.outboundservices.queryservices;

import com.oncontigo.api.profile.domain.model.entities.Doctor;
import com.oncontigo.api.profile.domain.model.queries.GetAllDoctorQuery;
import com.oncontigo.api.profile.domain.model.queries.GetDoctorByIdQuery;
import com.oncontigo.api.profile.domain.model.queries.GetDoctorByUserIdQuery;
import com.oncontigo.api.profile.domain.services.DoctorQueryService;
import com.oncontigo.api.profile.infrastructure.persistence.jpa.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorQueryServiceImpl implements DoctorQueryService {
    private final DoctorRepository doctorRepository;

    public DoctorQueryServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Optional<Doctor> handle(GetDoctorByIdQuery query) {
        return doctorRepository.findById(query.id());
    }

    @Override
    public List<Doctor> handle(GetAllDoctorQuery query) {
        return doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> handle(GetDoctorByUserIdQuery query) {
        return doctorRepository.findByUser_Id(query.userId());
    }
}