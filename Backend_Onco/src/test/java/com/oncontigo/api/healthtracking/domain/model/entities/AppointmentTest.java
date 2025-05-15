package com.oncontigo.api.healthtracking.domain.model.entities;

import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateAppointmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateAppointmentCommand;
import com.oncontigo.api.healthtracking.domain.model.valueobjects.AppointmentStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentTest {

    @Test
    public void test_appointment_constructor_initializes_fields_correctly() {
        // Arrange
        CreateAppointmentCommand command = mock(CreateAppointmentCommand.class);
        when(command.dateTime()).thenReturn(LocalDateTime.of(2023, 10, 1, 10, 0));
        when(command.description()).thenReturn("Initial description");
        HealthTracking healthTracking = mock(HealthTracking.class);

        // Act
        Appointment appointment = new Appointment(command, healthTracking);

        // Assert
        assertEquals(LocalDateTime.of(2023, 10, 1, 10, 0), appointment.getDateTime());
        assertEquals("Initial description", appointment.getDescription());
        assertEquals(AppointmentStatus.PENDING, appointment.getStatus());
        assertEquals(healthTracking, appointment.getHealthTracking());
    }


}