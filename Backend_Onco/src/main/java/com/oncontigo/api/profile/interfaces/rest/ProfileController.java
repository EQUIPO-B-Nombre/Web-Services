package com.oncontigo.api.profile.interfaces.rest;

import com.oncontigo.api.profile.domain.model.aggregates.Profile;
import com.oncontigo.api.profile.domain.model.commands.DeleteProfileCommand;
import com.oncontigo.api.profile.domain.model.commands.UpdateProfileCommand;
import com.oncontigo.api.profile.domain.model.queries.GetAllProfilesQuery;
import com.oncontigo.api.profile.domain.model.queries.GetProfileByIdQuery;
import com.oncontigo.api.profile.domain.model.queries.GetProfileByUserIdQuery;
import com.oncontigo.api.profile.domain.services.ProfileCommandService;
import com.oncontigo.api.profile.domain.services.ProfileQueryService;
import com.oncontigo.api.profile.interfaces.rest.resources.CreateProfileResource;
import com.oncontigo.api.profile.interfaces.rest.resources.ProfileResource;
import com.oncontigo.api.profile.interfaces.rest.resources.UpdateProfileResource;
import com.oncontigo.api.profile.interfaces.rest.transform.CreateProfileCommandFromResourceAssembler;
import com.oncontigo.api.profile.interfaces.rest.transform.ProfileResourceFromEntityAssembler;
import com.oncontigo.api.profile.interfaces.rest.transform.UpdateProfileCommandFromResourceAssembler;
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
@RequestMapping(value = "api/v1/profiles", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Endpoints para la gestión de perfiles")
public class ProfileController {
    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    public ProfileController(ProfileCommandService profileCommandService, ProfileQueryService profileQueryService) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
    }

    @GetMapping
    public ResponseEntity<List<ProfileResource>> getAllProfiles() {
        var query = new GetAllProfilesQuery();
        var profiles = profileQueryService.handle(query);
        var resources = profiles.stream().map(ProfileResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResource> getProfileById(@PathVariable Long id) {
        var query = new GetProfileByIdQuery(id);
        var profile = profileQueryService.handle(query);
        if (profile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/{userId}/user")
    public ResponseEntity<ProfileResource> getProfileByUserId(@PathVariable Long userId) {
        var query = new GetProfileByUserIdQuery(userId);
        var profile = profileQueryService.handle(query);
        if (profile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity<ProfileResource> createProfile(@RequestBody CreateProfileResource resource) {
        var command = CreateProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        Long profileId;
        try {
            profileId = profileCommandService.handle(command);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }
        var profile = profileQueryService.handle(new GetProfileByIdQuery(profileId));
        if (profile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var createdResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return new ResponseEntity<>(createdResource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResource> updateProfile(@PathVariable Long id, @RequestBody UpdateProfileResource resource) {
        var command = UpdateProfileCommandFromResourceAssembler.toCommandFromResource(id, resource);
        Optional<Profile> profile;
        try {
            profile = profileCommandService.handle(command);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        if (profile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var updatedResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(updatedResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        var command = new DeleteProfileCommand(id);
        try {
            profileCommandService.handle(command);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok("Perfil con id " + id + " eliminado exitosamente");
    }
}