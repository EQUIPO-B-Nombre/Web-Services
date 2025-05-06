package com.oncontigo.api.healthtracking.domain.exceptions;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(Long appointmentId) {
        super("Appointment con id " + appointmentId + " no encontrado");
    }
}