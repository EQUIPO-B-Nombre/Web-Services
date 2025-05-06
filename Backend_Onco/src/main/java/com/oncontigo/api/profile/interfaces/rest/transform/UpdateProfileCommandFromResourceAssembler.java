package com.oncontigo.api.profile.interfaces.rest.transform;

import com.oncontigo.api.profile.domain.model.commands.UpdateProfileCommand;
import com.oncontigo.api.profile.interfaces.rest.resources.UpdateProfileResource;

public class UpdateProfileCommandFromResourceAssembler {
    public static UpdateProfileCommand toCommandFromResource(Long profileId, UpdateProfileResource resource) {
        return new UpdateProfileCommand(
                profileId,
                resource.firstName(),
                resource.lastName(),
                resource.city(),
                resource.country(),
                resource.birthDate(),
                resource.description(),
                resource.photo(),
                resource.experience(),
                resource.dni(),
                resource.phone()
        );
    }
}