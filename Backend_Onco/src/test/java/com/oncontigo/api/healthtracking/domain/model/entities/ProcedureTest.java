package com.oncontigo.api.healthtracking.domain.model.entities;

import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateProcedureCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateProcedureCommand;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcedureTest {

    @Test
    public void test_procedure_constructor_initializes_fields_correctly() {
        // Arrange
        CreateProcedureCommand command = mock(CreateProcedureCommand.class);
        when(command.name()).thenReturn("Test Procedure");
        when(command.description()).thenReturn("Test Description");
        when(command.performedAt()).thenReturn(LocalDateTime.of(2023, 10, 1, 10, 0));
        HealthTracking healthTracking = mock(HealthTracking.class);

        // Act
        Procedure procedure = new Procedure(command, healthTracking);

        // Assert
        assertEquals("Test Procedure", procedure.getName());
        assertEquals("Test Description", procedure.getDescription());
        assertEquals(LocalDateTime.of(2023, 10, 1, 10, 0), procedure.getPerformedAt());
        assertEquals(healthTracking, procedure.getHealthTracking());
    }

    @Test
    public void test_procedure_constructor_throws_exception_when_command_is_null() {
        // Arrange
        HealthTracking healthTracking = mock(HealthTracking.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> new Procedure(null, healthTracking));
    }
}