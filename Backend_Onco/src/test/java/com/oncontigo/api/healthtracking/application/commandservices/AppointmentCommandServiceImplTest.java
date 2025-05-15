package com.oncontigo.api.healthtracking.application.commandservices;

import com.oncontigo.api.healthtracking.domain.exceptions.AppointmentNotFoundException;
import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateAppointmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.DeleteAppointmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateAppointmentCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Appointment;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.AppointmentRepository;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.HealthTrackingRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentCommandServiceImplTest {

    @Test
    public void test_handle_create_appointment_successfully() {
        // Arrange
        AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
        HealthTrackingRepository healthTrackingRepository = mock(HealthTrackingRepository.class);
        HealthTracking healthTracking = mock(HealthTracking.class);
        CreateAppointmentCommand command = mock(CreateAppointmentCommand.class);

        when(command.healthTrackingId()).thenReturn(1L);
        when(command.dateTime()).thenReturn(LocalDateTime.now().plusDays(1));
        when(command.description()).thenReturn("Test Appointment");
        when(healthTrackingRepository.findById(1L)).thenReturn(Optional.of(healthTracking));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> {
            Appointment appointment = invocation.getArgument(0);
            appointment.setId(100L); // Simula el ID asignado
            return appointment;
        });

        AppointmentCommandServiceImpl service = new AppointmentCommandServiceImpl(appointmentRepository, healthTrackingRepository);

        // Act
        Long id = service.handle(command);

        // Assert
        assertNotNull(id);
        assertEquals(100L, id);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    public void test_handle_update_appointment_successfully() {
        // Arrange
        AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
        HealthTrackingRepository healthTrackingRepository = mock(HealthTrackingRepository.class);
        Appointment appointment = mock(Appointment.class);
        HealthTracking healthTracking = mock(HealthTracking.class); // Mock de HealthTracking
        UpdateAppointmentCommand command = mock(UpdateAppointmentCommand.class);

        when(command.id()).thenReturn(1L);
        when(command.dateTime()).thenReturn(LocalDateTime.now().plusDays(2));
        when(command.description()).thenReturn("Updated Appointment");
        when(command.status()).thenReturn("COMPLETED");
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointment.getHealthTracking()).thenReturn(healthTracking); // Configura el HealthTracking

        AppointmentCommandServiceImpl service = new AppointmentCommandServiceImpl(appointmentRepository, healthTrackingRepository);

        // Act
        Optional<Appointment> updatedAppointment = service.handle(command);

        // Assert
        assertTrue(updatedAppointment.isPresent());
        verify(appointment).update(command);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    public void test_handle_delete_appointment_successfully() {
        // Arrange
        AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
        HealthTrackingRepository healthTrackingRepository = mock(HealthTrackingRepository.class);
        Appointment appointment = mock(Appointment.class);
        DeleteAppointmentCommand command = mock(DeleteAppointmentCommand.class);

        when(command.id()).thenReturn(1L);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        AppointmentCommandServiceImpl service = new AppointmentCommandServiceImpl(appointmentRepository, healthTrackingRepository);

        // Act
        service.handle(command);

        // Assert
        verify(appointmentRepository).delete(appointment);
    }

    @Test
    public void test_handle_create_throws_exception_when_healthtracking_not_found() {
        // Arrange
        AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
        HealthTrackingRepository healthTrackingRepository = mock(HealthTrackingRepository.class);
        CreateAppointmentCommand command = mock(CreateAppointmentCommand.class);

        when(command.healthTrackingId()).thenReturn(1L);
        when(healthTrackingRepository.findById(1L)).thenReturn(Optional.empty());

        AppointmentCommandServiceImpl service = new AppointmentCommandServiceImpl(appointmentRepository, healthTrackingRepository);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertEquals("HealthTracking not found with ID: 1", exception.getMessage());
    }


}