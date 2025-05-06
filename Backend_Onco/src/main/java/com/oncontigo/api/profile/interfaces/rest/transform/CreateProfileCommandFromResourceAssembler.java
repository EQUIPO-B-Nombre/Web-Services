package com.oncontigo.api.profile.interfaces.rest.transform;

import com.oncontigo.api.profile.domain.model.commands.CreateProfileCommand;
import com.oncontigo.api.profile.interfaces.rest.resources.CreateProfileResource;

public class CreateProfileCommandFromResourceAssembler {
    public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource) {
        return new CreateProfileCommand(
                resource.userId(),
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