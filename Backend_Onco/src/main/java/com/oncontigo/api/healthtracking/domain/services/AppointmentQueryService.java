package com.oncontigo.api.healthtracking.domain.services;

import com.oncontigo.api.healthtracking.domain.model.entities.Appointment;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAllAppointmentsByHealthTrackingIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAppointmentIdQuery;


import java.util.List;
import java.util.Optional;

public interface AppointmentQueryService {
    Optional<Appointment> handle(GetAppointmentIdQuery query);
    List<Appointment> handle(GetAllAppointmentsByHealthTrackingIdQuery query);
}