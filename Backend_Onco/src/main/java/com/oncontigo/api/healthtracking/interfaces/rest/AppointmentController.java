package com.oncontigo.api.healthtracking.interfaces.rest;

import com.oncontigo.api.healthtracking.domain.model.commands.CreateAppointmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.DeleteAppointmentCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateAppointmentCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Appointment;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAllAppointmentsByHealthTrackingIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAppointmentIdQuery;
import com.oncontigo.api.healthtracking.domain.services.AppointmentCommandService;
import com.oncontigo.api.healthtracking.domain.services.AppointmentQueryService;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.AppointmentResource;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.CreateAppointmentResource;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.UpdateAppointmentResource;
import com.oncontigo.api.healthtracking.interfaces.rest.transform.AppointmentResourceFromEntityAssembler;
import com.oncontigo.api.healthtracking.interfaces.rest.transform.CreateAppointmentCommandFromResourceAssembler;
import com.oncontigo.api.healthtracking.interfaces.rest.transform.UpdateAppointmentCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "api/v1/appointments", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Appointment", description = "Endpoints para la gestión de Appointment")
public class AppointmentController {

    private final AppointmentCommandService appointmentCommandService;
    private final AppointmentQueryService appointmentQueryService;

    public AppointmentController(AppointmentCommandService appointmentCommandService,
                                 AppointmentQueryService appointmentQueryService) {
        this.appointmentCommandService = appointmentCommandService;
        this.appointmentQueryService = appointmentQueryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResource> getAppointmentById(@PathVariable Long id) {
        var query = new GetAppointmentIdQuery(id);
        var appointment = appointmentQueryService.handle(query);
        if (appointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resource = AppointmentResourceFromEntityAssembler.toResource(appointment.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/healthtracking/{healthTrackingId}")
    public ResponseEntity<List<AppointmentResource>> getAllAppointmentsByHealthTrackingId(@PathVariable Long healthTrackingId) {
        var query = new GetAllAppointmentsByHealthTrackingIdQuery(healthTrackingId);
        var appointments = appointmentQueryService.handle(query);
        if (appointments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resources = appointments.stream()
                .map(AppointmentResourceFromEntityAssembler::toResource)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    public ResponseEntity<AppointmentResource> createAppointment(@RequestBody CreateAppointmentResource createAppointmentResource) {
        var createAppointmentCommand = CreateAppointmentCommandFromResourceAssembler.toCommand(createAppointmentResource);
        Long appointmentId;
        try {
            appointmentId = appointmentCommandService.handle(createAppointmentCommand);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating appointment", e);
        }

        var appointment = appointmentQueryService.handle(new GetAppointmentIdQuery(appointmentId));
        if (appointment.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var appointmentResource = AppointmentResourceFromEntityAssembler.toResource(appointment.get());
        return new ResponseEntity<>(appointmentResource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResource> updateAppointment(@PathVariable Long id,
                                                                 @RequestBody UpdateAppointmentResource updateAppointmentResource) {
        var updateAppointmentCommand = UpdateAppointmentCommandFromResourceAssembler.toCommand(id, updateAppointmentResource);
        Optional<Appointment> updatedAppointment;
        try {
            updatedAppointment = appointmentCommandService.handle(updateAppointmentCommand);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }

        if (updatedAppointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var appointmentResource = AppointmentResourceFromEntityAssembler.toResource(updatedAppointment.get());
        return ResponseEntity.ok(appointmentResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        try {
            appointmentCommandService.handle(new DeleteAppointmentCommand(id));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}