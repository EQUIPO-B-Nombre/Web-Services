package com.oncontigo.api.profile.domain.exceptions;

public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(Long id) {
        super("Dentist with id " + id + " not found");
    }
}