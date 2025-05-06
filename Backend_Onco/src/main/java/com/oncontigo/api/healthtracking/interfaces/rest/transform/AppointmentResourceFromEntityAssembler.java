package com.oncontigo.api.healthtracking.interfaces.rest.transform;

import com.oncontigo.api.healthtracking.domain.model.entities.Appointment;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.AppointmentResource;

public class AppointmentResourceFromEntityAssembler {

    public static AppointmentResource toResource(Appointment appointment) {
        return new AppointmentResource(
                appointment.getId(),
                appointment.getDateTime(),
                appointment.getDescription(),
                appointment.getHealthTrackingId()
        );
    }
}