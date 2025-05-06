package com.oncontigo.api.healthtracking.interfaces.rest;

import com.oncontigo.api.healthtracking.domain.exceptions.ProcedureNotFoundException;
import com.oncontigo.api.healthtracking.domain.model.commands.DeleteProcedureCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Procedure;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAllProceduresByHealthTrackingIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetProcedureByIdQuery;
import com.oncontigo.api.healthtracking.domain.services.ProcedureCommandService;
import com.oncontigo.api.healthtracking.domain.services.ProcedureQueryService;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.CreateProcedureResource;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.ProcedureResource;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.UpdateProcedureResource;
import com.oncontigo.api.healthtracking.interfaces.rest.transform.CreateProcedureCommandFromResourceAssembler;
import com.oncontigo.api.healthtracking.interfaces.rest.transform.ProcedureResourceFromEntityAssembler;
import com.oncontigo.api.healthtracking.interfaces.rest.transform.UpdateProcedureCommandFromResourceAssembler;
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
@RequestMapping(value = "api/v1/procedures", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Procedure", description = "Endpoints para la gestión de procedimientos")
public class ProcedureController {

    private final ProcedureCommandService procedureCommandService;
    private final ProcedureQueryService procedureQueryService;

    public ProcedureController(ProcedureCommandService procedureCommandService,
                               ProcedureQueryService procedureQueryService) {
        this.procedureCommandService = procedureCommandService;
        this.procedureQueryService = procedureQueryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcedureResource> getProcedureById(@PathVariable Long id) {
        var query = new GetProcedureByIdQuery(id);
        var procedure = procedureQueryService.handle(query);
        if (procedure.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resource = ProcedureResourceFromEntityAssembler.toResource(procedure.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/healthtracking/{healthTrackingId}")
    public ResponseEntity<List<ProcedureResource>> getAllProceduresByHealthTrackingId(@PathVariable Long healthTrackingId) {
        var query = new GetAllProceduresByHealthTrackingIdQuery(healthTrackingId);
        var procedures = procedureQueryService.handle(query);
        if (procedures.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resources = procedures.stream()
                .map(ProcedureResourceFromEntityAssembler::toResource)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    public ResponseEntity<ProcedureResource> createProcedure(@RequestBody CreateProcedureResource createProcedureResource) {
        var createProcedureCommand = CreateProcedureCommandFromResourceAssembler.toCommand(createProcedureResource);
        Long procedureId;
        try {
            procedureId = procedureCommandService.handle(createProcedureCommand);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating procedure", e);
        }

        var procedure = procedureQueryService.handle(new GetProcedureByIdQuery(procedureId));
        if (procedure.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var procedureResource = ProcedureResourceFromEntityAssembler.toResource(procedure.get());
        return new ResponseEntity<>(procedureResource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcedureResource> updateProcedure(@PathVariable Long id,
                                                             @RequestBody UpdateProcedureResource updateProcedureResource) {
        var updateProcedureCommand = UpdateProcedureCommandFromResourceAssembler.toCommand(id, updateProcedureResource);
        Optional<Procedure> updatedProcedure;
        try {
            updatedProcedure = procedureCommandService.handle(updateProcedureCommand);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }

        if (updatedProcedure.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var procedureResource = ProcedureResourceFromEntityAssembler.toResource(updatedProcedure.get());
        return ResponseEntity.ok(procedureResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcedure(@PathVariable Long id) {
        try {
            procedureCommandService.handle(new DeleteProcedureCommand(id));
            return ResponseEntity.noContent().build();
        } catch (ProcedureNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}