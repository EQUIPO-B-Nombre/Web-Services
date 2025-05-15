package com.oncontigo.api.healthtracking.application.commandservices;

import com.oncontigo.api.healthtracking.application.outboundservices.acl.ExternalProfilesService;
import com.oncontigo.api.healthtracking.domain.exceptions.PrescriptionNotFoundException;
import com.oncontigo.api.healthtracking.domain.model.commands.CreatePrescriptionCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.DeletePrescriptionCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdatePrescriptionCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Prescription;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.PrescriptionRepository;
import com.oncontigo.api.profile.domain.exceptions.DoctorNotFoundException;
import com.oncontigo.api.profile.domain.exceptions.PatientNotFoundException;
import com.oncontigo.api.profile.domain.model.entities.Doctor;
import com.oncontigo.api.profile.domain.model.entities.Patient;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrescriptionCommandServiceImplTest {

    @Test
    public void test_handle_create_prescription_successfully() {
        // Arrange
        PrescriptionRepository prescriptionRepository = mock(PrescriptionRepository.class);
        ExternalProfilesService externalProfilesService = mock(ExternalProfilesService.class);
        CreatePrescriptionCommand command = mock(CreatePrescriptionCommand.class);
        Doctor doctor = mock(Doctor.class);
        Patient patient = mock(Patient.class);

        when(command.doctorId()).thenReturn(1L);
        when(command.patientId()).thenReturn(2L);
        when(command.medicationName()).thenReturn("Test Medication");
        when(command.dosage()).thenReturn("2 pills daily");
        when(externalProfilesService.fetchDoctorById(1L)).thenReturn(Optional.of(doctor));
        when(externalProfilesService.fetchPatientById(2L)).thenReturn(Optional.of(patient));
        when(prescriptionRepository.save(any(Prescription.class))).thenAnswer(invocation -> {
            Prescription prescription = invocation.getArgument(0);
            prescription.setId(100L); // Simula el ID asignado
            return prescription;
        });

        PrescriptionCommandServiceImpl service = new PrescriptionCommandServiceImpl(prescriptionRepository, externalProfilesService);

        // Act
        Long id = service.handle(command);

        // Assert
        assertNotNull(id);
        assertEquals(100L, id);
        verify(prescriptionRepository).save(any(Prescription.class));
    }

    @Test
    public void test_handle_update_prescription_successfully() {
        // Arrange
        PrescriptionRepository prescriptionRepository = mock(PrescriptionRepository.class);
        UpdatePrescriptionCommand command = mock(UpdatePrescriptionCommand.class);
        Prescription prescription = mock(Prescription.class);

        when(command.id()).thenReturn(1L);
        when(command.medicationName()).thenReturn("Updated Medication");
        when(command.dosage()).thenReturn("1 pill daily");
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(prescription.update(command)).thenReturn(prescription); // Configura el mock para devolver el objeto actualizado
        when(prescriptionRepository.save(prescription)).thenReturn(prescription);

        PrescriptionCommandServiceImpl service = new PrescriptionCommandServiceImpl(prescriptionRepository, null);

        // Act
        Optional<Prescription> updatedPrescription = service.handle(command);

        // Assert
        assertTrue(updatedPrescription.isPresent());
        verify(prescription).update(command);
        verify(prescriptionRepository).save(prescription);
    }

    @Test
    public void test_handle_delete_prescription_successfully() {
        // Arrange
        PrescriptionRepository prescriptionRepository = mock(PrescriptionRepository.class);
        DeletePrescriptionCommand command = mock(DeletePrescriptionCommand.class);
        Prescription prescription = mock(Prescription.class);

        when(command.id()).thenReturn(1L);
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));

        PrescriptionCommandServiceImpl service = new PrescriptionCommandServiceImpl(prescriptionRepository, null);

        // Act
        service.handle(command);

        // Assert
        verify(prescriptionRepository).delete(prescription);
    }


}