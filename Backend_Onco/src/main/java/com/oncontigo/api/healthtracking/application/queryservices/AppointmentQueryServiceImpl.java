package com.oncontigo.api.healthtracking.application.queryservices;

import com.oncontigo.api.healthtracking.domain.model.entities.Appointment;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAllAppointmentsByHealthTrackingIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAppointmentIdQuery;
import com.oncontigo.api.healthtracking.domain.services.AppointmentQueryService;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentQueryServiceImpl implements AppointmentQueryService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentQueryServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Optional<Appointment> handle(GetAppointmentIdQuery query) {
        return appointmentRepository.findById(query.id());
    }

    @Override
    public List<Appointment> handle(GetAllAppointmentsByHealthTrackingIdQuery query) {
        return appointmentRepository.findByHealthTracking_Id(query.healthTrackingId());
    }
}