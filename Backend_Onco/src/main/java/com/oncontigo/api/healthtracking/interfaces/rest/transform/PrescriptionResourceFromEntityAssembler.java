package com.oncontigo.api.healthtracking.interfaces.rest.transform;

import com.oncontigo.api.healthtracking.domain.model.entities.Prescription;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.PrescriptionResource;



public class PrescriptionResourceFromEntityAssembler {

    public static PrescriptionResource toResource(Prescription prescription) {
        return new PrescriptionResource(
                prescription.getId(),
                prescription.getPatientId(),
                prescription.getDoctorId(),
                prescription.getMedicationName(),
                prescription.getDosage()
        );
    }
}