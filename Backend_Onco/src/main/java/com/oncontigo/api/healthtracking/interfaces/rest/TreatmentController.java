package com.oncontigo.api.healthtracking.interfaces.rest;

import com.oncontigo.api.healthtracking.domain.exceptions.TreatmentNotFoundException;
import com.oncontigo.api.healthtracking.domain.model.commands.DeleteTreatmentCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Treatment;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAllTreatmentsByHealthTrackingIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetTreatmentByIdQuery;
import com.oncontigo.api.healthtracking.domain.services.TreatmentCommandService;
import com.oncontigo.api.healthtracking.domain.services.TreatmentQueryService;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.CreateTreatmentResource;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.TreatmentResource;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.UpdateTreatmentResource;
import com.oncontigo.api.healthtracking.interfaces.rest.transform.CreateTreatmentCommandFromResourceAssembler;
import com.oncontigo.api.healthtracking.interfaces.rest.transform.TreatmentResourceFromEntityAssembler;
import com.oncontigo.api.healthtracking.interfaces.rest.transform.UpdateTreatmentCommandFromResourceAssembler;
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
@RequestMapping(value = "api/v1/treatments", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Treatment", description = "Endpoints para la gestión de tratamientos")
public class TreatmentController {

    private final TreatmentCommandService treatmentCommandService;
    private final TreatmentQueryService treatmentQueryService;

    public TreatmentController(TreatmentCommandService treatmentCommandService,
                               TreatmentQueryService treatmentQueryService) {
        this.treatmentCommandService = treatmentCommandService;
        this.treatmentQueryService = treatmentQueryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreatmentResource> getTreatmentById(@PathVariable Long id) {
        var query = new GetTreatmentByIdQuery(id);
        var treatment = treatmentQueryService.handle(query);
        if (treatment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resource = TreatmentResourceFromEntityAssembler.toResource(treatment.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/healthtracking/{healthTrackingId}")
    public ResponseEntity<List<TreatmentResource>> getAllTreatmentsByHealthTrackingId(@PathVariable Long healthTrackingId) {
        var query = new GetAllTreatmentsByHealthTrackingIdQuery(healthTrackingId);
        var treatments = treatmentQueryService.handle(query);
        if (treatments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resources = treatments.stream()
                .map(TreatmentResourceFromEntityAssembler::toResource)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    public ResponseEntity<TreatmentResource> createTreatment(@RequestBody CreateTreatmentResource createTreatmentResource) {
        var createTreatmentCommand = CreateTreatmentCommandFromResourceAssembler.toCommand(createTreatmentResource);
        Long treatmentId;
        try {
            treatmentId = treatmentCommandService.handle(createTreatmentCommand);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating treatment", e);
        }

        var treatment = treatmentQueryService.handle(new GetTreatmentByIdQuery(treatmentId));
        if (treatment.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var treatmentResource = TreatmentResourceFromEntityAssembler.toResource(treatment.get());
        return new ResponseEntity<>(treatmentResource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreatmentResource> updateTreatment(@PathVariable Long id,
                                                             @RequestBody UpdateTreatmentResource updateTreatmentResource) {
        var updateTreatmentCommand = UpdateTreatmentCommandFromResourceAssembler.toCommand(id, updateTreatmentResource);
        Optional<Treatment> updatedTreatment;
        try {
            updatedTreatment = treatmentCommandService.handle(updateTreatmentCommand);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }

        if (updatedTreatment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var treatmentResource = TreatmentResourceFromEntityAssembler.toResource(updatedTreatment.get());
        return ResponseEntity.ok(treatmentResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTreatment(@PathVariable Long id) {
        try {
            treatmentCommandService.handle(new DeleteTreatmentCommand(id));
            return ResponseEntity.noContent().build();
        } catch (TreatmentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}