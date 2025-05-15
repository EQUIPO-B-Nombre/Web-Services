package com.oncontigo.api.healthtracking.application.commandservices;

import com.oncontigo.api.healthtracking.domain.exceptions.TreatmentNotFoundException;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateTreatmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.DeleteTreatmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateTreatmentCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Treatment;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.HealthTrackingRepository;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.TreatmentRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TreatmentCommandServiceImplTest {

    @Test
    public void test_handle_create_treatment_successfully() {
        // Arrange
        TreatmentRepository treatmentRepository = mock(TreatmentRepository.class);
        HealthTrackingRepository healthTrackingRepository = mock(HealthTrackingRepository.class);
        CreateTreatmentCommand command = mock(CreateTreatmentCommand.class);
        var healthTracking = mock(com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking.class);

        when(command.healthTrackingId()).thenReturn(1L);
        when(command.name()).thenReturn("Test Treatment");
        when(command.description()).thenReturn("Test Description");
        when(command.startDate()).thenReturn(LocalDateTime.now());
        when(command.endDate()).thenReturn(LocalDateTime.now().plusDays(10));
        when(healthTrackingRepository.findById(1L)).thenReturn(Optional.of(healthTracking));
        when(treatmentRepository.save(any(Treatment.class))).thenAnswer(invocation -> {
            Treatment treatment = invocation.getArgument(0);
            treatment.setId(100L); // Simula el ID asignado
            return treatment;
        });

        TreatmentCommandServiceImpl service = new TreatmentCommandServiceImpl(treatmentRepository, healthTrackingRepository);

        // Act
        Long id = service.handle(command);

        // Assert
        assertNotNull(id);
        assertEquals(100L, id);
        verify(treatmentRepository).save(any(Treatment.class));
    }

    @Test
    public void test_handle_update_treatment_successfully() {
        // Arrange
        TreatmentRepository treatmentRepository = mock(TreatmentRepository.class);
        UpdateTreatmentCommand command = mock(UpdateTreatmentCommand.class);
        Treatment treatment = mock(Treatment.class);

        when(command.id()).thenReturn(1L);
        when(command.name()).thenReturn("Updated Treatment");
        when(command.description()).thenReturn("Updated Description");
        when(command.startDate()).thenReturn(LocalDateTime.now());
        when(command.endDate()).thenReturn(LocalDateTime.now().plusDays(5));
        when(treatmentRepository.findById(1L)).thenReturn(Optional.of(treatment));
        when(treatment.update(command)).thenReturn(treatment);
        when(treatmentRepository.save(treatment)).thenReturn(treatment);

        TreatmentCommandServiceImpl service = new TreatmentCommandServiceImpl(treatmentRepository, null);

        // Act
        Optional<Treatment> updatedTreatment = service.handle(command);

        // Assert
        assertTrue(updatedTreatment.isPresent());
        verify(treatment).update(command);
        verify(treatmentRepository).save(treatment);
    }

    @Test
    public void test_handle_delete_treatment_successfully() {
        // Arrange
        TreatmentRepository treatmentRepository = mock(TreatmentRepository.class);
        DeleteTreatmentCommand command = mock(DeleteTreatmentCommand.class);
        Treatment treatment = mock(Treatment.class);

        when(command.id()).thenReturn(1L);
        when(treatmentRepository.findById(1L)).thenReturn(Optional.of(treatment));

        TreatmentCommandServiceImpl service = new TreatmentCommandServiceImpl(treatmentRepository, null);

        // Act
        service.handle(command);

        // Assert
        verify(treatmentRepository).delete(treatment);
    }

    @Test
    public void test_handle_create_throws_exception_when_healthtracking_not_found() {
        // Arrange
        TreatmentRepository treatmentRepository = mock(TreatmentRepository.class);
        HealthTrackingRepository healthTrackingRepository = mock(HealthTrackingRepository.class);
        CreateTreatmentCommand command = mock(CreateTreatmentCommand.class);

        when(command.healthTrackingId()).thenReturn(1L);
        when(healthTrackingRepository.findById(1L)).thenReturn(Optional.empty());

        TreatmentCommandServiceImpl service = new TreatmentCommandServiceImpl(treatmentRepository, healthTrackingRepository);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertEquals("HealthTracking not found with ID: 1", exception.getMessage());
    }

    @Test
    public void test_handle_update_throws_exception_when_treatment_not_found() {
        // Arrange
        TreatmentRepository treatmentRepository = mock(TreatmentRepository.class);
        UpdateTreatmentCommand command = mock(UpdateTreatmentCommand.class);

        when(command.id()).thenReturn(1L);
        when(treatmentRepository.findById(1L)).thenReturn(Optional.empty());

        TreatmentCommandServiceImpl service = new TreatmentCommandServiceImpl(treatmentRepository, null);

        // Act & Assert
        TreatmentNotFoundException exception = assertThrows(TreatmentNotFoundException.class, () -> service.handle(command));
        assertEquals("Treatment with id 1 not found", exception.getMessage());
    }
}