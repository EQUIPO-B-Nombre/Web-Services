package com.oncontigo.api.healthtracking.application.commandservices;

import com.oncontigo.api.healthtracking.domain.exceptions.AppointmentNotFoundException;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateAppointmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.DeleteAppointmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateAppointmentCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Appointment;
import com.oncontigo.api.healthtracking.domain.services.AppointmentCommandService;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.AppointmentRepository;
import com.oncontigo.api.healthtracking.infrastructure.persistence.jap.repositories.HealthTrackingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentCommandServiceImpl implements AppointmentCommandService {

    private final AppointmentRepository appointmentRepository;
    private final HealthTrackingRepository healthTrackingRepository;

    public AppointmentCommandServiceImpl(AppointmentRepository appointmentRepository, HealthTrackingRepository healthTrackingRepository) {
        this.appointmentRepository = appointmentRepository;
        this.healthTrackingRepository = healthTrackingRepository;
    }

    @Override
    public Long handle(CreateAppointmentCommand command) {
        var healthTracking = healthTrackingRepository.findById(command.healthTrackingId());
        if (healthTracking.isEmpty()) throw new IllegalArgumentException("HealthTracking not found with ID: " + command.healthTrackingId());
        Appointment appointment = new Appointment(command, healthTracking.get());
        appointmentRepository.save(appointment);
        return appointment.getId();
    }

    @Override
    public Optional<Appointment> handle(UpdateAppointmentCommand command) {
        var appointment = appointmentRepository.findById(command.id());
        if (appointment.isEmpty()) throw new AppointmentNotFoundException(command.id());
        var updatedAppointment = appointment.get();
        appointmentRepository.save(updatedAppointment.update(command));
        return Optional.of(updatedAppointment);
    }

    @Override
    public void handle(DeleteAppointmentCommand command) {
        var appointment = appointmentRepository.findById(command.id());
        if (appointment.isEmpty()) throw new AppointmentNotFoundException(command.id());
        appointmentRepository.delete(appointment.get());
    }
}