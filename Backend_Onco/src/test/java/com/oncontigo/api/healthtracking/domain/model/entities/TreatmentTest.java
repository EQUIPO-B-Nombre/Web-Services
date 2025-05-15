package com.oncontigo.api.healthtracking.domain.model.entities;

import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateTreatmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateTreatmentCommand;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TreatmentTest {

    @Test
    public void test_treatment_constructor_initializes_fields_correctly() {
        // Arrange
        CreateTreatmentCommand command = mock(CreateTreatmentCommand.class);
        when(command.name()).thenReturn("Test Treatment");
        when(command.description()).thenReturn("Test Description");
        when(command.startDate()).thenReturn(LocalDateTime.of(2023, 10, 1, 10, 0));
        when(command.endDate()).thenReturn(LocalDateTime.of(2023, 12, 1, 10, 0));
        HealthTracking healthTracking = mock(HealthTracking.class);

        // Act
        Treatment treatment = new Treatment(command, healthTracking);

        // Assert
        assertEquals("Test Treatment", treatment.getName());
        assertEquals("Test Description", treatment.getDescription());
        assertEquals(LocalDateTime.of(2023, 10, 1, 10, 0), treatment.getStartDate());
        assertEquals(LocalDateTime.of(2023, 12, 1, 10, 0), treatment.getEndDate());
        assertEquals(healthTracking, treatment.getHealthTracking());
    }


    @Test
    public void test_treatment_constructor_throws_exception_when_command_is_null() {
        // Arrange
        HealthTracking healthTracking = mock(HealthTracking.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> new Treatment(null, healthTracking));
    }
}