package com.oncontigo.api.healthtracking.application.commandservices;

import com.oncontigo.api.healthtracking.domain.exceptions.ProcedureNotFoundException;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateProcedureCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.DeleteProcedureCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateProcedureCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Procedure;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.HealthTrackingRepository;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.ProcedureRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcedureCommandServiceImplTest {

    @Test
    public void test_handle_create_procedure_successfully() {
        // Arrange
        ProcedureRepository procedureRepository = mock(ProcedureRepository.class);
        HealthTrackingRepository healthTrackingRepository = mock(HealthTrackingRepository.class);
        CreateProcedureCommand command = mock(CreateProcedureCommand.class);
        var healthTracking = mock(com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking.class);

        when(command.healthTrackingId()).thenReturn(1L);
        when(command.name()).thenReturn("Test Procedure");
        when(command.description()).thenReturn("Test Description");
        when(command.performedAt()).thenReturn(LocalDateTime.now());
        when(healthTrackingRepository.findById(1L)).thenReturn(Optional.of(healthTracking));
        when(procedureRepository.save(any(Procedure.class))).thenAnswer(invocation -> {
            Procedure procedure = invocation.getArgument(0);
            procedure.setId(100L); // Simula el ID asignado
            return procedure;
        });

        ProcedureCommandServiceImpl service = new ProcedureCommandServiceImpl(procedureRepository, healthTrackingRepository);

        // Act
        Long id = service.handle(command);

        // Assert
        assertNotNull(id);
        assertEquals(100L, id);
        verify(procedureRepository).save(any(Procedure.class));
    }

    @Test
    public void test_handle_update_procedure_successfully() {
        // Arrange
        ProcedureRepository procedureRepository = mock(ProcedureRepository.class);
        UpdateProcedureCommand command = mock(UpdateProcedureCommand.class);
        Procedure procedure = mock(Procedure.class);

        when(command.id()).thenReturn(1L);
        when(command.name()).thenReturn("Updated Procedure");
        when(command.description()).thenReturn("Updated Description");
        when(command.performedAt()).thenReturn(LocalDateTime.now().plusDays(1));
        when(procedureRepository.findById(1L)).thenReturn(Optional.of(procedure));
        when(procedure.update(command)).thenReturn(procedure);
        when(procedureRepository.save(procedure)).thenReturn(procedure);

        ProcedureCommandServiceImpl service = new ProcedureCommandServiceImpl(procedureRepository, null);

        // Act
        Optional<Procedure> updatedProcedure = service.handle(command);

        // Assert
        assertTrue(updatedProcedure.isPresent());
        verify(procedure).update(command);
        verify(procedureRepository).save(procedure);
    }

    @Test
    public void test_handle_delete_procedure_successfully() {
        // Arrange
        ProcedureRepository procedureRepository = mock(ProcedureRepository.class);
        DeleteProcedureCommand command = mock(DeleteProcedureCommand.class);
        Procedure procedure = mock(Procedure.class);

        when(command.id()).thenReturn(1L);
        when(procedureRepository.findById(1L)).thenReturn(Optional.of(procedure));

        ProcedureCommandServiceImpl service = new ProcedureCommandServiceImpl(procedureRepository, null);

        // Act
        service.handle(command);

        // Assert
        verify(procedureRepository).delete(procedure);
    }

    @Test
    public void test_handle_update_throws_exception_when_procedure_not_found() {
        // Arrange
        ProcedureRepository procedureRepository = mock(ProcedureRepository.class);
        UpdateProcedureCommand command = mock(UpdateProcedureCommand.class);

        when(command.id()).thenReturn(1L);
        when(procedureRepository.findById(1L)).thenReturn(Optional.empty());

        ProcedureCommandServiceImpl service = new ProcedureCommandServiceImpl(procedureRepository, null);

        // Act & Assert
        ProcedureNotFoundException exception = assertThrows(ProcedureNotFoundException.class, () -> service.handle(command));
        assertEquals("Procedure with id 1 not found", exception.getMessage());
    }

    @Test
    public void test_handle_create_throws_exception_when_healthtracking_not_found() {
        // Arrange
        ProcedureRepository procedureRepository = mock(ProcedureRepository.class);
        HealthTrackingRepository healthTrackingRepository = mock(HealthTrackingRepository.class);
        CreateProcedureCommand command = mock(CreateProcedureCommand.class);

        when(command.healthTrackingId()).thenReturn(1L);
        when(healthTrackingRepository.findById(1L)).thenReturn(Optional.empty());

        ProcedureCommandServiceImpl service = new ProcedureCommandServiceImpl(procedureRepository, healthTrackingRepository);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertEquals("HealthTracking not found with ID: 1", exception.getMessage());
    }
}