package com.oncontigo.api.healthtracking.interfaces.rest;

import com.oncontigo.api.healthtracking.domain.model.commands.CreatePrescriptionCommand;
import com.oncontigo.api.healthtracking.domain.model.commands.UpdatePrescriptionCommand;
import com.oncontigo.api.healthtracking.domain.model.entities.Prescription;
import com.oncontigo.api.healthtracking.domain.model.queries.GetAllPrescriptionsByPatientIdQuery;
import com.oncontigo.api.healthtracking.domain.model.queries.GetPrescriptionByIdQuery;
import com.oncontigo.api.healthtracking.domain.services.PrescriptionCommandService;

import com.oncontigo.api.healthtracking.domain.services.PrescriptionQueryService;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.CreatePrescriptionResource;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.PrescriptionResource;
import com.oncontigo.api.healthtracking.interfaces.rest.resources.UpdatePrescriptionResource;
import com.oncontigo.api.healthtracking.interfaces.rest.transform.CreatePrescriptionCommandFromResourceAssembler;
import com.oncontigo.api.healthtracking.interfaces.rest.transform.PrescriptionResourceFromEntityAssembler;
import com.oncontigo.api.healthtracking.interfaces.rest.transform.UpdatePrescriptionCommandFromResourceAssembler;
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
@RequestMapping(value = "api/v1/prescriptions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Prescriptions", description = "Endpoints para la gestión de prescripciones")
public class PrescriptionController {

    private final PrescriptionCommandService prescriptionCommandService;
    private final PrescriptionQueryService prescriptionQueryService;

    public PrescriptionController(PrescriptionCommandService prescriptionCommandService,
                                  PrescriptionQueryService prescriptionQueryService) {
        this.prescriptionCommandService = prescriptionCommandService;
        this.prescriptionQueryService = prescriptionQueryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResource> getPrescriptionById(@PathVariable Long id) {
        var query = new GetPrescriptionByIdQuery(id);
        var prescription = prescriptionQueryService.handle(query);
        if (prescription.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resource = PrescriptionResourceFromEntityAssembler.toResource(prescription.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionResource>> getAllPrescriptionsByPatientId(@PathVariable Long patientId) {
        var query = new GetAllPrescriptionsByPatientIdQuery(patientId);
        var prescriptions = prescriptionQueryService.handle(query);
        if (prescriptions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resources = prescriptions.stream()
                .map(PrescriptionResourceFromEntityAssembler::toResource)
                .toList();
        return ResponseEntity.ok(resources);
    }


    @PostMapping
    public ResponseEntity<PrescriptionResource> createPrescription(@RequestBody CreatePrescriptionResource createPrescriptionResource) {
        var createPrescriptionCommand = CreatePrescriptionCommandFromResourceAssembler.toCommand(createPrescriptionResource);
        Long prescriptionId;
        try {
            prescriptionId = prescriptionCommandService.handle(createPrescriptionCommand);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating prescription", e);
        }

        var prescription = prescriptionQueryService.handle(new GetPrescriptionByIdQuery(prescriptionId));
        if (prescription.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var prescriptionResource = PrescriptionResourceFromEntityAssembler.toResource(prescription.get());
        return new ResponseEntity<>(prescriptionResource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionResource> updatePrescription(@PathVariable Long id,
                                                                   @RequestBody UpdatePrescriptionResource updatePrescriptionResource) {
        var updatePrescriptionCommand = UpdatePrescriptionCommandFromResourceAssembler.toCommand(id, updatePrescriptionResource);
        Optional<Prescription> updatedPrescription;
        try {
            updatedPrescription = prescriptionCommandService.handle(updatePrescriptionCommand);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }

        if (updatedPrescription.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var prescriptionResource = PrescriptionResourceFromEntityAssembler.toResource(updatedPrescription.get());
        return ResponseEntity.ok(prescriptionResource);
    }


}