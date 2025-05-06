package com.oncontigo.api.healthtracking.domain.exceptions;

public class ProcedureNotFoundException extends RuntimeException {
    public ProcedureNotFoundException(Long procedureId) {
        super("Procedure with id " + procedureId + " not found");
    }
}