package com.oncontigo.api.healthtracking.interfaces.rest;

import com.oncontigo.api.healthtracking.domain.exceptions.HealthTrackingNotFoundException;
import com.oncontigo.api.healthtracking.domain.model.commands.CreateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.DeleteHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdateHealthTrackingCommand;
import com.oncontigo.api.healthtracking.domain.model.aggregates.HealthTracking;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAllHealthTrackingsByDoctorIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetHealthTrackingByIdQuery;
import com.oncontigo.api.healthtracking.domain.services.HealthTrackingCommandService;
import com.oncontigo.api.healthtracking.domain.services.HealthTrackingQueryService;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.CreateHealthTrackingResource;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.HealthTrackingResource;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.UpdateHealthTrackingResource;
import com.oncontigo.api.healthtracking.interfaces.rest.transform.CreateHealthTrackingCommandFromResourceAssembler;
import com.oncontigo.api.healthtracking.interfaces.rest.transform.HealthTrackingResourceFromEntityAssembler;
import com.oncontigo.api.healthtracking.interfaces.rest.transform.UpdateHealthTrackingCommandFromResourceAssembler;
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
@RequestMapping(value = "api/v1/healthtrackings", produces = APPLICATION_JSON_VALUE)
@Tag(name = "HealthTracking", description = "Endpoints para la gestión de HealthTracking")
public class HealthTrackingController {

    private final HealthTrackingCommandService healthTrackingCommandService;
    private final HealthTrackingQueryService healthTrackingQueryService;

    public HealthTrackingController(HealthTrackingCommandService healthTrackingCommandService,
                                    HealthTrackingQueryService healthTrackingQueryService) {
        this.healthTrackingCommandService = healthTrackingCommandService;
        this.healthTrackingQueryService = healthTrackingQueryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<HealthTrackingResource> getHealthTrackingById(@PathVariable Long id) {
        var query = new GetHealthTrackingByIdQuery(id);
        var healthTracking = healthTrackingQueryService.handle(query);
        if (healthTracking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resource = HealthTrackingResourceFromEntityAssembler.toResource(healthTracking.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<HealthTrackingResource>> getAllHealthTrackingsByDoctorId(@PathVariable Long doctorId) {
        var query = new GetAllHealthTrackingsByDoctorIdQuery(doctorId);
        var healthTrackings = healthTrackingQueryService.handle(query);
        if (healthTrackings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resources = healthTrackings.stream()
                .map(HealthTrackingResourceFromEntityAssembler::toResource)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    public ResponseEntity<HealthTrackingResource> createHealthTracking(@RequestBody CreateHealthTrackingResource createHealthTrackingResource) {
        var createHealthTrackingCommand = CreateHealthTrackingCommandFromResourceAssembler.toCommand(createHealthTrackingResource);
        Long healthTrackingId;
        try {
            healthTrackingId = healthTrackingCommandService.handle(createHealthTrackingCommand);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating health tracking", e);
        }

        var healthTracking = healthTrackingQueryService.handle(new GetHealthTrackingByIdQuery(healthTrackingId));
        if (healthTracking.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var healthTrackingResource = HealthTrackingResourceFromEntityAssembler.toResource(healthTracking.get());
        return new ResponseEntity<>(healthTrackingResource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HealthTrackingResource> updateHealthTracking(@PathVariable Long id,
                                                                       @RequestBody UpdateHealthTrackingResource updateHealthTrackingResource) {
        var updateHealthTrackingCommand = UpdateHealthTrackingCommandFromResourceAssembler.toCommand(id, updateHealthTrackingResource);
        Optional<HealthTracking> updatedHealthTracking;
        try {
            updatedHealthTracking = healthTrackingCommandService.handle(updateHealthTrackingCommand);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }

        if (updatedHealthTracking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var healthTrackingResource = HealthTrackingResourceFromEntityAssembler.toResource(updatedHealthTracking.get());
        return ResponseEntity.ok(healthTrackingResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHealthTracking(@PathVariable Long id) {
        try {
            healthTrackingCommandService.handle(new DeleteHealthTrackingCommand(id));
            return ResponseEntity.noContent().build();
        } catch (HealthTrackingNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}