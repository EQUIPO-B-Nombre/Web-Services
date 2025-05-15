package com.oncontigo.api.healthtracking.domain.model.entities;

import com.oncontigo.api.healthtracking.domain.model.commands.CreatePrescriptionCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdatePrescriptionCommand;
import com.oncontigo.api.profile.domain.model.entities.Doctor;
import com.oncontigo.api.profile.domain.model.entities.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrescriptionTest {

    @Test
    public void test_prescription_constructor_initializes_fields_correctly() {
        // Arrange
        CreatePrescriptionCommand command = mock(CreatePrescriptionCommand.class);
        when(command.medicationName()).thenReturn("Test Medication");
        when(command.dosage()).thenReturn("2 pills daily");
        Patient patient = mock(Patient.class);
        Doctor doctor = mock(Doctor.class);

        // Act
        Prescription prescription = new Prescription(command, patient, doctor);

        // Assert
        assertEquals("Test Medication", prescription.getMedicationName());
        assertEquals("2 pills daily", prescription.getDosage());
        assertEquals(patient, prescription.getPatient());
        assertEquals(doctor, prescription.getDoctor());
    }


}