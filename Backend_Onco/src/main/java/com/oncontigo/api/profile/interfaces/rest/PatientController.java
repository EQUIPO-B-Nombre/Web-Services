package com.oncontigo.api.profile.interfaces.rest;

import com.oncontigo.api.profile.domain.model.entities.Patient;
import com.oncontigo.api.profile.domain.model.commands.DeletePatientCommand;
import com.oncontigo.api.profile.domain.model.queries.GetAllPatientsQuery;
import com.oncontigo.api.profile.domain.model.queries.GetPatientByIdQuery;
import com.oncontigo.api.profile.domain.services.PatientCommandService;
import com.oncontigo.api.profile.domain.services.PatientQueryService;
import com.oncontigo.api.profile.interfaces.rest.resources.PatientResource;
import com.oncontigo.api.profile.interfaces.rest.transform.PatientResourceFromEntityAssembler;
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
@RequestMapping(value = "api/v1/patients", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Patients", description = "Endpoints para la gestión de pacientes")
public class PatientController {
    private final PatientCommandService patientCommandService;
    private final PatientQueryService patientQueryService;

    public PatientController(PatientCommandService patientCommandService, PatientQueryService patientQueryService) {
        this.patientCommandService = patientCommandService;
        this.patientQueryService = patientQueryService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResource>> getAllPatients() {
        var query = new GetAllPatientsQuery();
        var patients = patientQueryService.handle(query);
        var resources = patients.stream().map(PatientResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResource> getPatientById(@PathVariable Long id) {
        var query = new GetPatientByIdQuery(id);
        var patient = patientQueryService.handle(query);
        if (patient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resource = PatientResourceFromEntityAssembler.toResourceFromEntity(patient.get());
        return ResponseEntity.ok(resource);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id) {
        var command = new DeletePatientCommand(id);
        try {
            patientCommandService.handle(command);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok("Paciente con id " + id + " eliminado exitosamente");
    }
}