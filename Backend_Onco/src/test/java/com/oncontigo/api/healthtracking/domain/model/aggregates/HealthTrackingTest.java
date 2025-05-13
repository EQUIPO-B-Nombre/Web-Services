package com.oncontigo.api.healthtracking.domain.model.aggregates;

import com.oncontigo.api.healthtracking.domain.model.commands.CreateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Appointment;
import com.oncontigo.api.profile.domain.model.entities.Doctor;
import com.oncontigo.api.profile.domain.model.entities.Patient;
import com.oncontigo.api.healthtracking.domain.model.valueobjects.HealthTrackingStatus;
import com.oncontigo.api.healthtracking.domain.model.valueobjects.AppointmentStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HealthTrackingTest {

    @Test
    public void test_healthtracking_constructor_initializes_fields_correctly() {
        // Arrange
        CreateHealthTrackingCommand command = mock(CreateHealthTrackingCommand.class);
        when(command.description()).thenReturn("Test description");
        Patient patient = mock(Patient.class);
        Doctor doctor = mock(Doctor.class);

        // Act
        HealthTracking healthTracking = new HealthTracking(command, patient, doctor);

        // Assert
        assertEquals(HealthTrackingStatus.ACTIVE, healthTracking.getStatus());
        assertEquals("Test description", healthTracking.getDescription());
        assertNull(healthTracking.getLastVisit());
        assertEquals(patient, healthTracking.getPatient());
        assertEquals(doctor, healthTracking.getDoctor());
    }

    @Test
    public void test_healthtracking_constructor_throws_exception_when_command_is_null() {
        // Arrange
        Patient patient = mock(Patient.class);
        Doctor doctor = mock(Doctor.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> new HealthTracking(null, patient, doctor));
    }

    @Test
    public void test_updateLastVisitIfCompleted_does_not_update_lastVisit_when_appointment_is_not_completed() {
        // Arrange
        HealthTracking healthTracking = new HealthTracking();
        Appointment appointment = mock(Appointment.class);
        when(appointment.getStatus()).thenReturn(AppointmentStatus.PENDING);

        // Act
        healthTracking.updateLastVisitIfCompleted(appointment);

        // Assert
        assertNull(healthTracking.getLastVisit());
    }
}