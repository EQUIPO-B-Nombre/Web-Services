package com.oncontigo.api.profile.interfaces.rest;

import com.oncontigo.api.profile.domain.model.commands.DeleteDoctorCommand;
import com.oncontigo.api.profile.domain.model.queries.GetAllDoctorQuery;
import com.oncontigo.api.profile.domain.model.queries.GetDoctorByIdQuery;
import com.oncontigo.api.profile.domain.services.DoctorCommandService;
import com.oncontigo.api.profile.domain.services.DoctorQueryService;
import com.oncontigo.api.profile.interfaces.rest.resources.DoctorResource;
import com.oncontigo.api.profile.interfaces.rest.transform.DentistResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "api/v1/doctors", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Doctors", description = "Endpoints para la gestión de dentistas")
public class DoctorController {
    private final DoctorCommandService dentistCommandService;
    private final DoctorQueryService doctorQueryService;

    public DoctorController(DoctorCommandService doctorCommandService, DoctorQueryService doctorQueryService) {
        this.dentistCommandService = doctorCommandService;
        this.doctorQueryService = doctorQueryService;
    }

    @GetMapping
    public ResponseEntity<List<DoctorResource>> getAllDentists() {
        var query = new GetAllDoctorQuery();
        var dentists = doctorQueryService.handle(query);
        var resources = dentists.stream().map(DentistResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResource> getDentistById(@PathVariable Long id) {
        var query = new GetDoctorByIdQuery(id);
        var dentist = doctorQueryService.handle(query);
        if (dentist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resource = DentistResourceFromEntityAssembler.toResourceFromEntity(dentist.get());
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDentist(@PathVariable Long id) {
        var command = new DeleteDoctorCommand(id);
        try {
            dentistCommandService.handle(command);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok("Dentista con id " + id + " eliminado exitosamente");
    }
}