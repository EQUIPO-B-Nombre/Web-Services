package com.oncontigo.api.healthtracking.application.commandservices;

import com.oncontigo.api.healthtracking.application.outboundservices.acl.ExternalProfilesService;
import com.oncontigo.api.healthtracking.domain.exceptions.DuplicateHealthTrackingException;


import com.oncontigo.api.healthtracking.domain.model.commands.CreateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.HealthTrackingRepository;
import com.oncontigo.api.profile.domain.exceptions.DoctorNotFoundException;
import com.oncontigo.api.profile.domain.model.entities.Doctor;
import com.oncontigo.api.profile.domain.model.entities.Patient;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HealthTrackingCommandServiceImplTest {

    @Test
    public void test_handle_create_throws_exception_if_healthtracking_exists() {
        // Arrange
        HealthTrackingRepository repository = mock(HealthTrackingRepository.class);
        ExternalProfilesService profilesService = mock(ExternalProfilesService.class);
        when(repository.existsByPatient_IdAndDoctor_Id(1L, 2L)).thenReturn(true);

        HealthTrackingCommandServiceImpl service = new HealthTrackingCommandServiceImpl(repository, profilesService);
        CreateHealthTrackingCommand command = new CreateHealthTrackingCommand(1L, 2L, "Test description");

        // Act & Assert
        assertThrows(DuplicateHealthTrackingException.class, () -> service.handle(command));
        verify(repository).existsByPatient_IdAndDoctor_Id(1L, 2L);
    }

    @Test
    public void test_handle_create_throws_exception_if_doctor_not_found() {
        // Arrange
        HealthTrackingRepository repository = mock(HealthTrackingRepository.class);
        ExternalProfilesService profilesService = mock(ExternalProfilesService.class);
        when(repository.existsByPatient_IdAndDoctor_Id(1L, 2L)).thenReturn(false);
        when(profilesService.fetchDoctorById(2L)).thenReturn(Optional.empty());

        HealthTrackingCommandServiceImpl service = new HealthTrackingCommandServiceImpl(repository, profilesService);
        CreateHealthTrackingCommand command = new CreateHealthTrackingCommand(1L, 2L, "Test description");

        // Act & Assert
        assertThrows(DoctorNotFoundException.class, () -> service.handle(command));
        verify(profilesService).fetchDoctorById(2L);
    }

    @Test
    public void test_handle_create_creates_healthtracking_successfully() {
        // Arrange
        HealthTrackingRepository repository = mock(HealthTrackingRepository.class);
        ExternalProfilesService profilesService = mock(ExternalProfilesService.class);
        Patient patient = mock(Patient.class);
        Doctor doctor = mock(Doctor.class);
        HealthTracking healthTracking = new HealthTracking(new CreateHealthTrackingCommand(1L, 2L, "Test description"), patient, doctor);
        healthTracking.setId(100L); // Simula que el ID se asigna después de guardar

        when(repository.existsByPatient_IdAndDoctor_Id(1L, 2L)).thenReturn(false);
        when(profilesService.fetchDoctorById(2L)).thenReturn(Optional.of(doctor));
        when(profilesService.fetchPatientById(1L)).thenReturn(Optional.of(patient));
        when(repository.save(any(HealthTracking.class))).thenAnswer(invocation -> {
            HealthTracking saved = invocation.getArgument(0);
            saved.setId(100L); // Asigna un ID simulado
            return saved;
        });

        HealthTrackingCommandServiceImpl service = new HealthTrackingCommandServiceImpl(repository, profilesService);
        CreateHealthTrackingCommand command = new CreateHealthTrackingCommand(1L, 2L, "Test description");

        // Act
        Long id = service.handle(command);

        // Assert
        assertNotNull(id);
        assertEquals(100L, id); // Verifica que el ID sea el esperado
        verify(repository).save(any(HealthTracking.class));
    }
}